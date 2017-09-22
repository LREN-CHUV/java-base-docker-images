package eu.humanbrainproject.mip.algorithms.rapidminer;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.rapidminer.operator.learner.PredictionModel;
import eu.humanbrainproject.mip.algorithms.ResultsFormat;
import eu.humanbrainproject.mip.algorithms.db.OutputDataConnector;
import eu.humanbrainproject.mip.algorithms.rapidminer.models.RapidMinerModel;
import eu.humanbrainproject.mip.algorithms.rapidminer.serializers.pfa.RapidMinerAlgorithmSerializer;
import eu.humanbrainproject.mip.algorithms.rapidminer.serializers.pfa.RapidMinerModelSerializer;


/**
 * Entrypoint
 *
 * @author Arnaud Jutzeler
 */
public class Main {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    private final String modelClassName;
    private final String modelSerializerClassName;
    private final String algorithmSerializerClassName;

    public Main(String modelClassName, String modelSerializerClassName, String algorithmSerializerClassName) {
        this.modelClassName = modelClassName;
        this.modelSerializerClassName = modelSerializerClassName;
        this.algorithmSerializerClassName = algorithmSerializerClassName;
    }

    public void run() {
        try {
            Class<?> modelClass = Class.forName(modelClassName);
            @SuppressWarnings("unchecked") RapidMinerModel<PredictionModel> model = (RapidMinerModel<PredictionModel>) modelClass.newInstance();

            Class<?> modelSerializerClass = Class.forName(modelSerializerClassName);
            @SuppressWarnings("unchecked") RapidMinerModelSerializer<PredictionModel> modelSerializer = (RapidMinerModelSerializer<PredictionModel>) modelSerializerClass.newInstance();

            Class<?> algorithmSerializerClass = Class.forName(algorithmSerializerClassName);
            RapidMinerAlgorithmSerializer algorithmSerializer = (RapidMinerAlgorithmSerializer) algorithmSerializerClass.getConstructor(RapidMinerModelSerializer.class).newInstance(modelSerializer);

            InputData inputData = InputData.fromEnv();

            // Run experiment
            RapidMinerAlgorithm<?> experiment = new RapidMinerAlgorithm<>(inputData, model, algorithmSerializer);

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

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Cannot execute the algorithm", e);
        }
    }

    public static void main(String[] args) {

        try {
            String settingsPath = (args.length == 0) ? "settings.properties" : args[0];

            Properties settings = new Properties();
            settings.load(Main.class.getResourceAsStream(settingsPath));

            Main main = new Main(settings.getProperty("model"),
                    settings.getProperty("modelSerializer"),
                    settings.getProperty("algorithmSerializer", RapidMinerAlgorithmSerializer.class.getName()));

            main.run();

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Cannot execute the algorithm", e);
        }
    }

}
