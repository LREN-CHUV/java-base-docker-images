package eu.humanbrainproject.mip.algorithms.chaos;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import eu.humanbrainproject.mip.algorithms.Algorithm;
import eu.humanbrainproject.mip.algorithms.Configuration;
import eu.humanbrainproject.mip.algorithms.db.DBException;


/**
 *
 *
 * @author Ludovic Claude
 */
public class ChaosAlgorithm implements Algorithm {

    private static final Logger LOGGER = Logger.getLogger(ChaosAlgorithm.class.getName());

    private static final String NAME = "chaos";
    private static final String DOCUMENTATION = "Chaos algorithm";
    private static final Map<String, String> PARAMS = Collections.singletonMap("failure", "training_fails");

    private static final Set<AlgorithmCapability> CAPABILITIES = new HashSet<>();

    static {
        CAPABILITIES.add(AlgorithmCapability.PREDICTIVE_MODEL);
        CAPABILITIES.add(AlgorithmCapability.CLASSIFICATION);
    }

    private InputData input;
    private Exception exception;
    private String pfa;


    /**
     * @param input
     */
    public ChaosAlgorithm(InputData input) {
        this.input = input;
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

    public Map<String, String> getParameters() {
        return Configuration.INSTANCE.algorithmParameterValues(PARAMS);
    }

    @Override
    public Set<AlgorithmCapability> getCapabilities() {
        return CAPABILITIES;
    }

    public void run() throws Exception {

        try {
            if (exception != null) {
                LOGGER.warning("This algorithm was already executed!");
                return;
            }

            if (input == null) {
                input = InputData.fromEnv();
            }

            final String failure = getParameters().get("failure");

            switch (failure) {
                case "training_fails": throw new Exception("Training failed");
                case "never_stop":
                    while (true) {
                        Thread.sleep(1000);
                    }
                case "no_results":
                    pfa = null;
                    break;
                case "invalid_json":
                    pfa = readResource("/example_02_invalid-json/model.pfa");
                    break;
                case "invalid_pfa_syntax":
                    pfa = readResource("/example_03_invalid-pfa-syntax/model.pfa");
                    break;
                case "invalid_pfa_semantics":
                    pfa = readResource("/example_04_invalid-pfa-semantics/model.pfa");
                    break;
                default: throw new Exception("Failure was not defined. Let's fail anyway");
            }


        } catch (DBException ex) {
            this.exception = ex;
            throw ex;
        }
    }

    /**
     * Generate the PFA representation of the experiment outcome
     */
    public String toPFA() {
        return pfa;
    }

    private String readResource(String resourceName) throws IOException {
        return Resources.toString(this.getClass().getResource(resourceName), Charsets.UTF_8);
    }

}
