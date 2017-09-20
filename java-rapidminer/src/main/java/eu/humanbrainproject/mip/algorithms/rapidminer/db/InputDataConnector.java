package eu.humanbrainproject.mip.algorithms.rapidminer.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InputDataConnector extends DBConnector {

    private final String query;

    public InputDataConnector(String query, DBConnectionDescriptor dbConnectionDescriptor) {
        super(dbConnectionDescriptor);
        this.query = query;
    }

    public ResultSet fetchInputData() throws DBException {
        return select(query);
    }

    void beforeSelect(Connection conn) throws SQLException {
        conn.setAutoCommit(false);
        // TODO The seed must be passed as a query parameters and generated above
        conn.prepareStatement("SELECT setseed(0.67)").execute();
    }

    void afterSelect(Connection conn) throws SQLException {
        conn.commit();
        conn.setAutoCommit(true);
    }

}
