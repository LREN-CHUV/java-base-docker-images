package eu.humanbrainproject.mip.algorithms.db;

import java.sql.* ;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles connections to a database
 *
 * @author Arnaud Jutzeler
 */
public class DBConnector implements AutoCloseable {
    private static final Logger LOGGER = Logger.getLogger(DBConnector.class.getName());

    private final DBConnectionDescriptor dbConnectionDescriptor;

    private transient Connection conn = null;

    public DBConnector(DBConnectionDescriptor dbConnectionDescriptor) {
        this.dbConnectionDescriptor = dbConnectionDescriptor;
    }

    protected <M> M select(String query, Function<ResultSet, M> transform) throws DBException {
        try {
            openConnection();

            beforeSelect(conn);

            M m;
            try (Statement stmt = conn.createStatement()) {
                try (ResultSet rs = stmt.executeQuery(query)) {
                    m = transform.apply(rs);
                }

                afterSelect(conn);
            }

            return m;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Cannot execute command on database", e);
            disconnect();

            throw new DBException(e);
        }
    }

    protected void beforeSelect(Connection conn) throws SQLException {
        // Override if necessary
    }

    protected void afterSelect(Connection conn) throws SQLException {
        // Override if necessary
    }

    private void openConnection() throws SQLException {
        if (conn != null && conn.isClosed()) {
            conn = null;
        }
        if (conn == null) {
            conn = dbConnectionDescriptor.getConnection();
        }
    }

    protected Connection getConnection() throws SQLException {
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
    }

    @Override
    public void close() throws Exception {
        disconnect();
    }

}
