package eu.humanbrainproject.mip.algorithms.rapidminer.db;

import java.sql.Connection;
import java.sql.SQLException;

public class InputDataConnector extends DBConnector {

    public InputDataConnector(String query, DBConnectionFactory dbConnectionFactory) {
        super(query, dbConnectionFactory);
    }

    public InputDataConnector(String query) {
        this(query, DBConnectionDirection.DATA_IN);
    }

    void onConnect(Connection conn) throws SQLException {
        // TODO The seed must be passed as a query parameters and generated above
        conn.prepareStatement("SELECT setseed(0.67)").execute();
    }
}
