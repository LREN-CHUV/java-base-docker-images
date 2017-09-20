package eu.humanbrainproject.mip.algorithms.rapidminer.db;

import java.sql.* ;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.lang.AutoCloseable;

/**
 *
 * @author Arnaud Jutzeler
 *
 */
public class DBConnector implements AutoCloseable {
    private static final Logger LOGGER = Logger.getLogger(DBConnector.class.getName());

    private final DBConnectionDescriptor dbConnectionDescriptor;

    private transient Connection conn = null;
    private transient ResultSet rs = null;

    public DBConnector(DBConnectionDescriptor dbConnectionDescriptor) {
        this.dbConnectionDescriptor = dbConnectionDescriptor;
    }

    ResultSet select(String query) throws DBException {
        try {
            openConnection();

            beforeSelect(conn);

            try (Statement stmt = conn.createStatement()) {
                rs = stmt.executeQuery(query);

                afterSelect(conn);
            }

            return rs;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Cannot execute command on database", e);
            disconnect();

            throw new DBException(e);
        }
    }

    void beforeSelect(Connection conn) throws SQLException {
    }

    void afterSelect(Connection conn) throws SQLException {
    }

    private void openConnection() throws SQLException {
        if (conn != null && conn.isClosed()) {
            conn = null;
        }
        if (conn == null) {
            conn = dbConnectionDescriptor.getConnection();
        }
    }

    Connection getConnection() throws SQLException {
        openConnection();
        return conn;
    }

    public void disconnect() {

        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "Cannot close DB connection", e);
            }
            conn = null;
        }
        if (rs != null) {
            try {
                if (!rs.isClosed()) {
                    rs.close();
                }
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "Cannot close resultset", e);
            }
            rs = null;
        }
    }

    @Override
    public void close() throws Exception {
        disconnect();
    }

}
