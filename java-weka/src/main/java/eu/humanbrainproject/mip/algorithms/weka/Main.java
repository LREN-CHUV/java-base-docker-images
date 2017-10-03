package eu.humanbrainproject.mip.algorithms.weka;

import eu.humanbrainproject.mip.algorithms.ResultsFormat;
import eu.humanbrainproject.mip.algorithms.db.OutputDataConnector;
import eu.humanbrainproject.mip.algorithms.weka.serializers.pfa.WekaAlgorithmSerializer;
import eu.humanbrainproject.mip.algorithms.weka.serializers.pfa.WekaClassifierSerializer;
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

    private final WekaClassifier<Classifier> classifier;
    private final WekaAlgorithmSerializer<Classifier> algorithmSerializer;

    @SuppressWarnings("unchecked")
    public Main(String classifierClassName, String classifierSerializerClassName, String algorithmSerializerClassName) throws Exception {
        classifier = new WekaClassifier<>(classifierClassName);

        Class<?> classifierSerializerClass = Class.forName(classifierSerializerClassName);
        WekaClassifierSerializer<Classifier> classifierSerializer = (WekaClassifierSerializer<Classifier>) classifierSerializerClass.newInstance();

        Class<?> algorithmSerializerClass = Class.forName(algorithmSerializerClassName);
        algorithmSerializer = (WekaAlgorithmSerializer<Classifier>) algorithmSerializerClass.getConstructor(WekaClassifierSerializer.class).newInstance(classifierSerializer);
    }

    public void run() {
        try {

            InputData inputData = InputData.fromEnv();

            // Run experiment
            WekaAlgorithm<Classifier> experiment = new WekaAlgorithm<>(inputData, classifier, algorithmSerializer);

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

            Main main = new Main(settings.getProperty("classifier"),
                    settings.getProperty("classifierSerializer"),
                    settings.getProperty("algorithmSerializer", WekaAlgorithmSerializer.class.getName()));

            main.run();

        } catch (ClassNotFoundException e) {
            LOGGER.severe(e.getMessage());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Cannot execute the algorithm", e);
        }
    }

}
