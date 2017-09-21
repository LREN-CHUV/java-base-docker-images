package eu.humanbrainproject.mip.algorithms.rapidminer;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import eu.humanbrainproject.mip.algorithms.ResultsFormat;
import eu.humanbrainproject.mip.algorithms.db.DBException;
import eu.humanbrainproject.mip.algorithms.db.OutputDataConnector;
import eu.humanbrainproject.mip.algorithms.rapidminer.exceptions.RapidMinerException;
import eu.humanbrainproject.mip.algorithms.rapidminer.models.RapidMinerModel;


/**
 * Entrypoint
 *
 * @author Arnaud Jutzeler
 */
public class Main {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {

        try {
            Class modelClass = Class.forName(args[0]);
            RapidMinerModel model = (RapidMinerModel) modelClass.newInstance();

            // Run experiment
            RapidMinerExperiment experiment = new RapidMinerExperiment(model);

            try {
                experiment.run();
            } finally {

                // Write results PFA in DB - it can represent also an error
                String pfa = experiment.toPFA();
                OutputDataConnector out = OutputDataConnector.fromEnv();
                out.saveResults(pfa, ResultsFormat.PFA_JSON);
            }

        } catch (ClassNotFoundException e) {
            LOGGER.severe(e.getMessage());

        } catch (InstantiationException | IllegalAccessException |
                IOException | DBException | ClassCastException | RapidMinerException e) {
            LOGGER.log(Level.SEVERE, "Cannot execute the algorithm", e);
        }
    }
}
