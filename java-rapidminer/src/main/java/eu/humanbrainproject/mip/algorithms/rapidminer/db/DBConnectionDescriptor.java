package eu.humanbrainproject.mip.algorithms.rapidminer.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnectionDescriptor {

    public static DBConnectionDescriptor inputConnector() {
        return new DBConnectionDescriptor(
                env("IN_JDBC_URL"),
                env("IN_JDBC_USER"),
                env("IN_JDBC_PASSWORD"));
    }

    public static DBConnectionDescriptor outputConnector() {
        return new DBConnectionDescriptor(
                env("OUT_JDBC_URL"),
                env("OUT_JDBC_USER"),
                env("OUT_JDBC_PASSWORD"));
    }

    private final String url;
    private final String user;
    private final String pass;

    DBConnectionDescriptor(String url, String user, String pass) {
        this.url = url;
        this.user = user;
        this.pass = pass;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, pass);
    }

    private static String env(String s) {
        return System.getenv(s);
    }

}
