package eu.humanbrainproject.mip.algorithms;

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
        return env("PARAM_variables").split(",");
    }

    public String[] covariables() {
        return env("PARAM_covariables").split(",");
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


    private static String env(String key) {
        // Read first system property then env variables
        return System.getProperty(key, System.getenv(key));
    }

    private static String env(String key, String defaultValue) {
        // Read first system property then env variables
        return System.getProperty(key, System.getenv().getOrDefault(key, defaultValue));
    }

}
