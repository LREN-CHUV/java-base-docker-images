package eu.humanbrainproject.mip.algorithms.weka;

import eu.humanbrainproject.mip.algorithms.ResultsFormat;
import eu.humanbrainproject.mip.algorithms.db.OutputDataConnector;
import eu.humanbrainproject.mip.algorithms.weka.models.WekaClassifier;
import eu.humanbrainproject.mip.algorithms.weka.serializers.pfa.WekaAlgorithmSerializer;
import eu.humanbrainproject.mip.algorithms.weka.serializers.pfa.WekaModelSerializer;
import weka.classifiers.Classifier;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Entrypoint
 *
 * @author Ludovic Claude
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
            @SuppressWarnings("unchecked") WekaClassifier<Classifier> model = (WekaClassifier<Classifier>) modelClass.newInstance();

            Class<?> modelSerializerClass = Class.forName(modelSerializerClassName);
            @SuppressWarnings("unchecked") WekaModelSerializer<Classifier> modelSerializer = (WekaModelSerializer<Classifier>) modelSerializerClass.newInstance();

            Class<?> algorithmSerializerClass = Class.forName(algorithmSerializerClassName);
            WekaAlgorithmSerializer algorithmSerializer = (WekaAlgorithmSerializer) algorithmSerializerClass.getConstructor(WekaModelSerializer.class).newInstance(modelSerializer);

            InputData inputData = InputData.fromEnv();

            // Run experiment
            WekaAlgorithm<?> experiment = new WekaAlgorithm<>(inputData, model, algorithmSerializer);

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
                    settings.getProperty("algorithmSerializer", WekaAlgorithmSerializer.class.getName()));

            main.run();

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Cannot execute the algorithm", e);
        }
    }

}
