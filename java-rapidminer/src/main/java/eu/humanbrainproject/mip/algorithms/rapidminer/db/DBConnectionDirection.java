package eu.humanbrainproject.mip.algorithms.rapidminer.db;

import java.sql.Connection;
import java.sql.SQLException;

public enum DBConnectionDirection implements DBConnectionFactory {

    DATA_IN(env("IN_JDBC_URL"),
            env("IN_JDBC_USER"),
            env("IN_JDBC_PASSWORD")),

    DATA_OUT(env("OUT_JDBC_URL"),
            env("OUT_JDBC_USER"),
            env("OUT_JDBC_PASSWORD"));

    private static final String OUT_TABLE = System.getenv().getOrDefault("RESULT_TABLE", "job_result");

    private final DBConnectionDescriptor descriptor;

    DBConnectionDirection(String url, String user, String password) {
        this.descriptor = new DBConnectionDescriptor(url, user, password);
    }

    public Connection getConnection() throws SQLException {
        return descriptor.getConnection();
    }

    private static String env(String s) {
        return System.getenv(s);
    }
}
