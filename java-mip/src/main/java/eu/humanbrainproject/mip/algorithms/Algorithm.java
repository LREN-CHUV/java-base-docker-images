package eu.humanbrainproject.mip.algorithms;

/**
 * An algorithm is used to learn from input data and produce a result.
 */
public interface Algorithm {

    String getName();
    String getDocumentation();

    /**
     * If there was an error during training, this method should return an error message; null otherwise.
     * @return the error message or null
     */
    String getErrorMessage();

}
