package eu.humanbrainproject.mip.algorithms.rapidminer.db;

import java.io.IOException;
import java.sql.* ;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.lang.AutoCloseable;
import eu.humanbrainproject.mip.algorithms.rapidminer.RapidMinerExperiment;

/**
 *
 * @author Arnaud Jutzeler
 *
 */
public class DBConnector implements AutoCloseable {
    private static final Logger LOGGER = Logger.getLogger(DBConnector.class.getName());

    private final DBConnectionFactory dbConnectionFactory;

    private transient Connection conn = null;
    private transient Statement stmt = null;
    private transient ResultSet rs = null;

    public DBConnector(DBConnectionFactory dbConnectionFactory) {
        this.dbConnectionFactory = dbConnectionFactory;
    }

    public ResultSet select(String query) throws DBException {
        try {
            openConnection();

            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);
            conn.commit();
            conn.setAutoCommit(true);
            return rs;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Cannot execute command on database", e);
            disconnect();

            throw new DBException(e);
        }
    }

    private void openConnection() throws SQLException {
        if (conn == null) {
            conn = dbConnectionFactory.getConnection();
            conn.setAutoCommit(false);
            onConnect(conn);
        }
    }

    void onConnect(Connection conn) throws SQLException {
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
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "Cannot close JDBC statement", e);
            }
            stmt = null;
        }
        if (rs != null) {
            try {
                rs.close();
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

    public static void saveResults(RapidMinerExperiment experiment)
            throws DBException {

        Statement stmt = null;
        try (Connection conn = DBConnectionDirection.DATA_OUT.getConnection()) {

            String jobId = System.getProperty("JOB_ID", System.getenv("JOB_ID"));
            String node = System.getenv("NODE");
            //String outFormat = System.getenv("OUT_FORMAT");
            String function = System.getenv().getOrDefault("FUNCTION", "JAVA");

            String shape = "pfa_json";
            // Escape single quote with another single quote (SQL)
            String pfa = experiment.toPFA().replaceAll("'", "''");

            String statement = String.format("INSERT INTO %s (job_id, node, data, shape, function) VALUES ('%s', '%s', '%s', '%s', '%s')",
                    OUT_TABLE, jobId, node, pfa, shape, function);

            Statement st = conn.createStatement();

            st.executeUpdate(statement);

        } catch (SQLException | IOException e) {
            throw new DBException(e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {}
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {}
            }
        }
    }

    public static class DBResults {

        public String node;
        public String shape;
        public String data;

        public DBResults(String node, String shape, String data) {
            this.node = node;
            this.shape = shape;
            this.data = data;
        }
    }

    public static DBResults getDBResult(String jobId) throws DBException {

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {

            String TABLE = System.getenv().getOrDefault("RESULT_TABLE", "job_result");
            conn = DBConnectionDirection.DATA_OUT.getConnection();

            Statement st = conn.createStatement();
            rs = st.executeQuery(String.format("SELECT node, data, shape FROM %s WHERE job_id ='%s'", TABLE, jobId));

            DBResults results = null;
            while (rs.next()) {
                results = new DBResults(rs.getString("node"), rs.getString("shape"), rs.getString("data"));
            }

            return results;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DBException(e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {}
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {}
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {}
            }
        }
    }
}
