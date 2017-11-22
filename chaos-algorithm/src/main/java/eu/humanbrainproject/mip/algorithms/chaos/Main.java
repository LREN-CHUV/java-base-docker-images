package eu.humanbrainproject.mip.algorithms.chaos;

import java.util.logging.Level;
import java.util.logging.Logger;

import eu.humanbrainproject.mip.algorithms.ResultsFormat;
import eu.humanbrainproject.mip.algorithms.db.OutputDataConnector;


/**
 * Entrypoint
 *
 * @author Ludovic Claude
 */
public class Main {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    public Main() {
    }

    private void run() throws Exception {
        InputData inputData = InputData.fromEnv();

        // Run algorithm
        ChaosAlgorithm algorithm = new ChaosAlgorithm(inputData);

        try {
            algorithm.run();

        } finally {

            // Write results PFA in DB - it can represent also an error
            String pfa = algorithm.toPFA();
            if (pfa != null) {
                OutputDataConnector out = OutputDataConnector.fromEnv();
                out.saveResults(pfa, ResultsFormat.PFA_JSON);
            }
        }
    }

    public static void main(String[] args) {

        try {

            Main main = new Main();

            main.run();

        } catch (ClassNotFoundException e) {
            LOGGER.severe(e.getMessage());
            System.exit(1);

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Cannot execute the algorithm: " + e.getMessage(), e);
            System.exit(1);
        }
    }

}
