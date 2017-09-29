package eu.humanbrainproject.mip.algorithms;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * An algorithm that supports nullable input
 */
public class NullableInputAlgorithm implements Algorithm {
    private String errorMessage;

    @Override
    public String getName() {
        return "simple";
    }

    @Override
    public String getDocumentation() {
        return "A simple algorithm";
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public Set<AlgorithmCapability> getCapabilities() {
        return new HashSet<>(Arrays.asList(
                AlgorithmCapability.PREDICTIVE_MODEL,
                AlgorithmCapability.CLASSIFICATION,
                AlgorithmCapability.INPUT_DATA_MISSING_VALUES
        ));
    }
}
