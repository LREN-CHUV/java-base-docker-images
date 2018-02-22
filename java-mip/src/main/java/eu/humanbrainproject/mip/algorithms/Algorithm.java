package eu.humanbrainproject.mip.algorithms;

import java.util.Set;

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

    Set<AlgorithmCapability> getCapabilities();

    enum AlgorithmCapability {
        /**
         * The algorithm can generate a predictive model, encoded as a PFA document
         */
        PREDICTIVE_MODEL,
        /**
         * The algorithm provides classification
         */
        CLASSIFICATION,
        /**
         * The algorithm provides multi-target classification
         */
        CLASSIFICATION_MT,
        /**
         * The algorithm provides regression
         */
        REGRESSION,
        /**
         * The algorithm provides multi-target regression
         */
        REGRESSION_MT,
        /**
         * The algorithm provides time-series prediction
         */
        TIME_SERIES,
        /**
         * The algorithm provides statistical information about the input data
         */
        STATISTICAL,
        /**
         * Can handle missing values in input data
         */
        INPUT_DATA_MISSING_VALUES,
        /**
         * The algorithm provides one or more visualisations
         */
        VISUALISATION,
        /**
         * The algorithm provides feature importances
         */
        FEATURE_IMPORTANCE        
    }
}
