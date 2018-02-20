
package eu.humanbrainproject.mip.algorithms.jsi;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Files;

import eu.humanbrainproject.mip.algorithms.jsi.common.ClusAlgorithm;
import eu.humanbrainproject.mip.algorithms.jsi.common.ClusMeta;
import eu.humanbrainproject.mip.algorithms.jsi.common.InputData;
import eu.humanbrainproject.mip.algorithms.jsi.serializers.pfa.ClusModelPFASerializer;
import eu.humanbrainproject.mip.algorithms.jsi.serializers.pfa.ClusVisualizationSerializer;
import si.ijs.kt.clus.model.ClusModel;
import eu.humanbrainproject.mip.algorithms.Configuration;
import eu.humanbrainproject.mip.algorithms.ResultsFormat;
import eu.humanbrainproject.mip.algorithms.db.OutputDataConnector;


/**
 * Entry point
 *
 * @author Martin Breskvar
 */
public class Main<M extends ClusModel> {

    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    private final ClusModelPFASerializer<M> algorithmSerializer;
    private final ClusMeta clusMeta;
    private final ClusVisualizationSerializer<M> algorithmVisualizationSerializer;


    public Main(ClusModelPFASerializer<M> algorithmSerializer, ClusMeta clusMeta, ClusVisualizationSerializer<M> algorithmVisualizationSerializer) {
        this.algorithmSerializer = algorithmSerializer;
        this.clusMeta = clusMeta;
        this.algorithmVisualizationSerializer = algorithmVisualizationSerializer;
    }


    public void run() throws Exception {

        LOGGER.log(Level.FINEST, "Strating the CLUS library");
        LOGGER.log(Level.FINEST, "Preparing WEKA database props");

        // weka properties
        Path targetDbProps = FileSystems.getDefault().getPath("/opt", "weka", "props", "weka", "experiment", "DatabaseUtils.props");
        if (Configuration.INSTANCE.inputJdbcUrl().startsWith("jdbc:postgresql:")) {
            Path dbProps = FileSystems.getDefault().getPath("/opt", "weka", "databases-props", "DatabaseUtils.props.postgresql");
            Files.createLink(targetDbProps, dbProps);
        }

        InputData input = InputData.fromEnv();

        ClusAlgorithm<M> experiment = new ClusAlgorithm<>(input, algorithmSerializer, clusMeta);

        String visualization = "";
        try {
            experiment.run();

            // generate vizualization if it exists
            if (algorithmVisualizationSerializer != null) {
                visualization = algorithmVisualizationSerializer.getVisualizationString(experiment.getModel());
            }
        }
        catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Cannot execute the algorithm", e);
        }
        finally {
            // get constructed PFA and save to database
            String pfa = experiment.toPFA();

            LOGGER.log(Level.FINEST, "Saving PFA to database");
            // Write results PFA in DB - it can represent also an error
            OutputDataConnector out = OutputDataConnector.fromEnv();
            out.saveResults(pfa, ResultsFormat.PFA_JSON);

            if (visualization != "") // if algo has a visualization
            {
                LOGGER.log(Level.FINEST, "Saving VISUALIZATION to database");
                out.saveResults(visualization, ResultsFormat.JAVASCRIPT_VISJS);
            }
        }
    }
}
