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

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void run() throws Exception {
        Class<?> modelClass = Class.forName(modelClassName);
        RapidMinerModel<PredictionModel> model = (RapidMinerModel<PredictionModel>) modelClass.newInstance();

        Class<?> modelSerializerClass = Class.forName(modelSerializerClassName);
        RapidMinerModelSerializer<PredictionModel> modelSerializer = (RapidMinerModelSerializer<PredictionModel>) modelSerializerClass.newInstance();

        Class<?> algorithmSerializerClass = Class.forName(algorithmSerializerClassName);
        RapidMinerAlgorithmSerializer algorithmSerializer = (RapidMinerAlgorithmSerializer) algorithmSerializerClass.getConstructor(RapidMinerModelSerializer.class).newInstance(modelSerializer);

        InputData inputData = InputData.fromEnv();

        // Run algorithm
		RapidMinerAlgorithm<?> algorithm = new RapidMinerAlgorithm<>(inputData, model, algorithmSerializer);

        try {
            algorithm.run();
        } finally {

            // Write results PFA in DB - it can represent also an error
            String pfa = algorithm.toPFA();
            OutputDataConnector out = OutputDataConnector.fromEnv();
            out.saveResults(pfa, ResultsFormat.PFA_JSON);
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

        } catch (ClassNotFoundException e) {
            LOGGER.severe(e.getMessage());
            System.exit(1);

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Cannot execute the algorithm: " + e.getMessage(), e);
            System.exit(1);
        }
    }

}
