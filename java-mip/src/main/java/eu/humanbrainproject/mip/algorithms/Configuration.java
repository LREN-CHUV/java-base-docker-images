package eu.humanbrainproject.mip.algorithms;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Configuration of the algorithms running in MIP Woken execution runtime.
 *
 * The configuration is done via a set of environment variables.
 */
public class Configuration {

    public static final Configuration INSTANCE = new Configuration();

    private Configuration() {
    }

    public String inputJdbcUrl() {
        return env("IN_JDBC_URL");
    }

    public String inputJdbcUser() {
        return env("IN_JDBC_USER");
    }

    public String inputJdbcPassword() {
        return env("IN_JDBC_PASSWORD");
    }

    public String inputSqlQuery() {
        return env("PARAM_query", "");
    }

    public String outputJdbcUrl() {
        return env("OUT_JDBC_URL");
    }

    public String outputJdbcUser() {
        return env("OUT_JDBC_USER");
    }

    public String outputJdbcPassword() {
        return env("OUT_JDBC_PASSWORD");
    }

    public String outputResultTable() {
        return env("RESULT_TABLE", "job_result");
    }

    public String[] variables() {

        final String variables = env("PARAM_variables");
        if (variables == null) {
            throw new RuntimeException("Environment parameter PARAM_variables is empty");
        }
        return variables.split(",");
    }

    public String[] covariables() {
        final String covariables = env("PARAM_covariables");
        if (covariables == null) {
            throw new RuntimeException("Environment parameter PARAM_covariables is empty");
        }
        return covariables.split(",");
    }

    public double randomSeed() {
        return Double.valueOf(env("PARAM_seed", "0.67"));
    }

    public String jobId() {
        return env("JOB_ID");
    }

    public String executionNode() {
        return env("NODE");
    }

    public String function() {
        return env("FUNCTION", "JAVA");
    }

    public String dockerImage() {
        return env("DOCKER_IMAGE", "");
    }

    /**
     * Return the values of the algorithm parameters read from the environment variables.
     * For a parameter k, we read first the value of environment variable MODEL_PARAM_k,
     * then for backwards compatibility the value of environment variable PARAM_MODEL_k
     */
    public Map<String, String> algorithmParameterValues(Set<String> algorithmParameters) {
        HashMap<String, String> values = new HashMap<>();
        for (String param: algorithmParameters) {
            values.put(param, env("MODEL_PARAM_" + param, env("PARAM_MODEL_" + param)));
        }
        return values;
    }

    /**
     * Return the values of the algorithm parameters read from the environment variables.
     * For a parameter k, we read first the value of environment variable MODEL_PARAM_k,
     * then for backwards compatibility the value of environment variable PARAM_MODEL_k.
     * If no matching environment variable is found, then the value of k in the input map is used.
     */
    public Map<String, String> algorithmParameterValues(Map<String, String> algorithmParametersWithDefaultValues) {
        HashMap<String, String> values = new HashMap<>();
        for (String param: algorithmParametersWithDefaultValues.keySet()) {
            values.put(param, env("MODEL_PARAM_" + param, env("PARAM_MODEL_" + param, algorithmParametersWithDefaultValues.get(param))));
        }
        return values;
    }

    private static String env(String key) {
        // Read first system property then env variables
        return System.getProperty(key, System.getenv(key));
    }

    private static String env(String key, String defaultValue) {
        // Read first system property then env variables
        return System.getProperty(key, System.getenv().getOrDefault(key, defaultValue));
    }

}
