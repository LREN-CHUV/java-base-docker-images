package eu.humanbrainproject.mip.algorithms.weka;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import eu.humanbrainproject.mip.algorithms.Algorithm;
import eu.humanbrainproject.mip.algorithms.weka.serializers.pfa.WekaAlgorithmSerializer;
import weka.classifiers.Classifier;
import weka.core.Capabilities;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;


/**
 * Default experiment consisting of training and validating a specific classifier
 *
 * @author Ludovic Claude
 */
public class WekaAlgorithm<M extends Classifier> implements Algorithm {

    private static final Logger LOGGER = Logger.getLogger(WekaAlgorithm.class.getName());

    private static final String NAME = "weka";
    private static final String DOCUMENTATION = "Weka Model";

    private InputData input;
    private WekaClassifier<M> classifier;
    private WekaAlgorithmSerializer<M> serializer;
    private HashSet<AlgorithmCapability> capabilities;
    private Exception exception;

    /**
     * @param input
     * @param classifier
     */
    public WekaAlgorithm(InputData input, WekaClassifier<M> classifier, WekaAlgorithmSerializer<M> serializer) {
        this.input = input;
        this.classifier = classifier;
        this.serializer = serializer;
        this.capabilities = new HashSet<>(Arrays.asList(
                AlgorithmCapability.PREDICTIVE_MODEL,
                AlgorithmCapability.CLASSIFICATION
        ));
        final Capabilities wekaCapabilities = classifier.getCapabilities();
        if (wekaCapabilities.handles(Capabilities.Capability.MISSING_VALUES)) {
            this.capabilities.add(AlgorithmCapability.INPUT_DATA_MISSING_VALUES);
            LOGGER.info("Missing input values supported");
        }
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
    public WekaClassifier<M> getClassifier() {
        return classifier;
    }

    @Override
    public Set<AlgorithmCapability> getCapabilities() {
        return capabilities;
    }

    public void run() throws Exception {

        try {
            if (classifier.isAlreadyTrained() || exception != null) {
                LOGGER.warning("This experiment was already executed!");
                return;
            }

            if (input == null) {
                input = InputData.fromEnv();
            }

            // Train the classifier
            classifier.train(input);

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

    @SuppressWarnings("RedundantCast")
    private ObjectMapper getObjectMapper() {
        ObjectMapper myObjectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule("Weka", new Version(1, 0, 0, null, null, null));
        module.addSerializer((Class<? extends WekaAlgorithm<M>>) this.getClass(), serializer);
        myObjectMapper.registerModule(module);
        return myObjectMapper;
    }

}
