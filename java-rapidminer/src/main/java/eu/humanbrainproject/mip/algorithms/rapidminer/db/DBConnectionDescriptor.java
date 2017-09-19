package eu.humanbrainproject.mip.algorithms.rapidminer.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

class DBConnectionDescriptor implements DBConnectionFactory {

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

}
