package eu.humanbrainproject.mip.algorithms.weka;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import eu.humanbrainproject.mip.algorithms.Algorithm;
import eu.humanbrainproject.mip.algorithms.weka.serializers.pfa.WekaAlgorithmSerializer;
import weka.classifiers.Classifier;

import java.io.IOException;
import java.util.logging.Logger;


/**
 * Default experiment consisting of training and validating a specific model
 *
 * @author Ludovic Claude
 */
public class WekaAlgorithm<M extends Classifier> implements Algorithm {

    private static final Logger LOGGER = Logger.getLogger(WekaAlgorithm.class.getName());

    private static final String NAME = "weka";
    private static final String DOCUMENTATION = "Weka Model";

    private InputData input;
    private WekaClassifier<M> model;
    private WekaAlgorithmSerializer serializer;

    public Exception exception;

    /**
     * @param input
     * @param model
     */
    public WekaAlgorithm(InputData input, WekaClassifier<M> model, WekaAlgorithmSerializer serializer) {
        this.input = input;
        this.model = model;
        this.serializer = serializer;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getDocumentation() {
        return DOCUMENTATION;
    }

    @Override
    public String getErrorMessage() {
        if (exception != null) {
            return exception.getMessage();
        } else {
            return null;
        }
    }

    /**
     * @return
     */
    public InputData getInput() {
        return input;
    }

    /**
     * @return
     */
    public WekaClassifier<M> getModel() {
        return model;
    }

    public void run() throws Exception {

        try {
            if (model.isAlreadyTrained() || exception != null) {
                LOGGER.warning("This experiment was already executed!");
                return;
            }

            if (input == null) {
                input = InputData.fromEnv();
            }

            // Train the model
            model.train(input);

        } catch (Exception ex) {
            this.exception = ex;
            throw ex;
        }
    }

    /**
     * Generate the PFA representation of the experiment outcome
     *
     * @return
     * @throws IOException
     */
    public String toPFA() throws IOException {
        ObjectMapper myObjectMapper = getObjectMapper();
        return myObjectMapper.writeValueAsString(this);
    }

    /**
     * Generate the PFA representation of the experiment outcome
     *
     * @return
     * @throws IOException
     */
    public String toPrettyPFA() throws IOException {
        ObjectMapper myObjectMapper = getObjectMapper();
        return myObjectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
    }

    private ObjectMapper getObjectMapper() {
        ObjectMapper myObjectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule("Weka", new Version(1, 0, 0, null, null, null));
        module.addSerializer(WekaAlgorithm.class, serializer);
        myObjectMapper.registerModule(module);
        return myObjectMapper;
    }

}
