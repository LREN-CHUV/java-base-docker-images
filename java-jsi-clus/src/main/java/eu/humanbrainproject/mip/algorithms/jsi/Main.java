
package eu.humanbrainproject.mip.algorithms.jsi;

import java.util.logging.Level;
import java.util.logging.Logger;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Files;

import eu.humanbrainproject.mip.algorithms.jsi.common.ClusAlgorithm;
import eu.humanbrainproject.mip.algorithms.jsi.common.ClusMeta;
import eu.humanbrainproject.mip.algorithms.jsi.common.InputData;
import eu.humanbrainproject.mip.algorithms.jsi.serializers.pfa.ClusFimpSerializer;
import eu.humanbrainproject.mip.algorithms.jsi.serializers.pfa.ClusModelPFASerializer;
import eu.humanbrainproject.mip.algorithms.jsi.serializers.pfa.ClusVisualizationSerializer;
import eu.humanbrainproject.mip.algorithms.jsi.common.ClusConstants;

import si.ijs.kt.clus.ext.featureRanking.Fimp;
import si.ijs.kt.clus.model.ClusModel;

import eu.humanbrainproject.mip.algorithms.Configuration;
import eu.humanbrainproject.mip.algorithms.ResultsFormat;
import eu.humanbrainproject.mip.algorithms.Algorithm.AlgorithmCapability;
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
    private final ClusFimpSerializer algorithmFimpSerializer;


    public Main(ClusModelPFASerializer<M> algorithmSerializer, ClusMeta clusMeta, ClusVisualizationSerializer<M> algorithmVisualizationSerializer, ClusFimpSerializer algorithmFimpSerializer) {
        this.algorithmSerializer = algorithmSerializer;
        this.clusMeta = clusMeta;
        this.algorithmVisualizationSerializer = algorithmVisualizationSerializer;
        this.algorithmFimpSerializer = algorithmFimpSerializer;
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
        String importances = "";
        OutputDataConnector out = null;
        try {
            experiment.run();

            out = OutputDataConnector.fromEnv();

            // generate vizualization if it exists
            if (algorithmVisualizationSerializer != null && experiment.getCapabilities().contains(AlgorithmCapability.VISUALISATION)) {
                visualization = algorithmVisualizationSerializer.getVisualizationString(experiment.getModel());
            }

            if (algorithmFimpSerializer != null && experiment.getCapabilities().contains(AlgorithmCapability.FEATURE_IMPORTANCE)) {
                // get feature importances if they exist
                if (new File(ClusConstants.CLUS_FIMPFILE).exists()) {
                    si.ijs.kt.clus.ext.featureRanking.Fimp fimp = new Fimp(ClusConstants.CLUS_FIMPFILE);
                    importances = algorithmFimpSerializer.getFimpString(fimp);
                }
            }
        }
        catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Cannot execute the algorithm", e);
        }
        finally {
            // if algorithm is predictive, get constructed PFA and save it to database
            if (experiment.getCapabilities().contains(AlgorithmCapability.PREDICTIVE_MODEL)) {
                String pfa = experiment.toPFA();

                LOGGER.log(Level.FINEST, "Saving PFA to database");
                // Write results PFA in DB - it can represent also an error
                out.saveResults(pfa, ResultsFormat.PFA_JSON);
            }

            // if algorithm has a visualization
            if (visualization != "") {
                LOGGER.log(Level.FINEST, "Saving VISUALIZATION to database");
                out.saveResults(visualization, ResultsFormat.JAVASCRIPT_VISJS);
            }

            // if algorithm produced feature importances
            if (importances != "") {
                LOGGER.log(Level.FINEST, "Saving IMPORTANCES to database");
                out.saveResults(importances, ResultsFormat.PFA_JSON);
            }
        }
    }
}
