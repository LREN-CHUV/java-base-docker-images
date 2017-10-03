package eu.humanbrainproject.mip.algorithms.weka;

import eu.humanbrainproject.mip.algorithms.Configuration;
import eu.humanbrainproject.mip.algorithms.ResultsFormat;
import eu.humanbrainproject.mip.algorithms.db.OutputDataConnector;
import eu.humanbrainproject.mip.algorithms.weka.serializers.pfa.WekaAlgorithmSerializer;
import eu.humanbrainproject.mip.algorithms.weka.serializers.pfa.WekaClassifierSerializer;
import weka.classifiers.Classifier;

import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Entrypoint
 *
 * @author Ludovic Claude
 */
public class Main<M extends Classifier> {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    private final WekaClassifier<M> classifier;
    private final WekaAlgorithmSerializer<M> algorithmSerializer;

    public Main(WekaClassifier<M> classifier, WekaAlgorithmSerializer<M> algorithmSerializer) {
        this.classifier = classifier;
        this.algorithmSerializer = algorithmSerializer;
    }

    public void run() {
        try {

            InputData inputData = InputData.fromEnv();

            // Run experiment
            WekaAlgorithm<M> experiment = new WekaAlgorithm<>(inputData, classifier, algorithmSerializer);

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

    @SuppressWarnings("unchecked")
    public static void main(String[] args) {

        try {
            String settingsPath = (args.length == 0) ? "settings.properties" : args[0];

            Properties settings = new Properties();
            settings.load(Main.class.getResourceAsStream(settingsPath));

            final String classifierClassName = settings.getProperty("classifier");
            WekaClassifier<Classifier> classifier = new WekaClassifier<>(classifierClassName);

            final String classifierSerializerClassName = settings.getProperty("classifierSerializer");
            Class<?> classifierSerializerClass = Class.forName(classifierSerializerClassName);
            WekaClassifierSerializer<Classifier> classifierSerializer = (WekaClassifierSerializer<Classifier>) classifierSerializerClass.newInstance();

            final String algorithmSerializerClassName = settings.getProperty("algorithmSerializer", WekaAlgorithmSerializer.class.getName());
            Class<?> algorithmSerializerClass = Class.forName(algorithmSerializerClassName);
            WekaAlgorithmSerializer<Classifier> algorithmSerializer = (WekaAlgorithmSerializer<Classifier>)
                    algorithmSerializerClass.getConstructor(WekaClassifierSerializer.class).newInstance(classifierSerializer);

            Path targetDbProps = FileSystems.getDefault().getPath("/opt", "weka", "props", "weka", "experiment", "DatabaseUtils.props");
            if (Configuration.INSTANCE.inputJdbcUrl().startsWith("jdbc:postgresql:")) {
                Path dbProps = FileSystems.getDefault().getPath("/opt", "weka", "databases-props", "DatabaseUtils.props.postgresql");
                Files.createLink(targetDbProps, dbProps);
            }
            Main main = new Main(classifier, algorithmSerializer);

            main.run();

        } catch (ClassNotFoundException e) {
            LOGGER.severe(e.getMessage());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Cannot execute the algorithm", e);
        }
    }

}
