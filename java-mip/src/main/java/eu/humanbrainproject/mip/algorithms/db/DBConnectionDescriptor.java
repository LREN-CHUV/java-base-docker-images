package eu.humanbrainproject.mip.algorithms.db;

import eu.humanbrainproject.mip.algorithms.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnectionDescriptor {
    private static final Configuration conf = Configuration.INSTANCE;

    private final String url;
    private final String user;
    private final String pass;

    public DBConnectionDescriptor(String url, String user, String pass) {
        this.url = url;
        this.user = user;
        this.pass = pass;
    }

    protected Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, pass);
    }

    /**
     * @return the input connector defined by the configuration
     */
    public static DBConnectionDescriptor inputConnectorFromEnv() {
        return new DBConnectionDescriptor(
                conf.inputJdbcUrl(),
                conf.inputJdbcUser(),
                conf.inputJdbcPassword());
    }

    /**
     * @return the output connector defined by the configuration
     */
    public static DBConnectionDescriptor outputConnectorFromEnv() {
        return new DBConnectionDescriptor(
                conf.outputJdbcUrl(),
                conf.outputJdbcUser(),
                conf.outputJdbcPassword());
    }

}
