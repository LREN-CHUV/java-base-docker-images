package eu.humanbrainproject.mip.algorithms.rapidminer.db;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OutputDataConnector extends DBConnector {
    private static final Logger LOGGER = Logger.getLogger(OutputDataConnector.class.getName());

    private final String outTable;

    public OutputDataConnector(DBConnectionDescriptor dbConnectionDescriptor, String outTable) {
        super(dbConnectionDescriptor);
        this.outTable = outTable;
    }

    public OutputDataConnector() {
        this(DBConnectionDescriptor.outputConnector(),
                System.getenv().getOrDefault("RESULT_TABLE", "job_result"));
    }

    public JobResults getJobResults(String jobId) throws DBException {
        String query = String.format("SELECT node, data, shape FROM %s WHERE job_id ='%s'", outTable, jobId);

        try (ResultSet rs = select(query)) {

            if (rs.next()) {
                return new JobResults(rs.getString("node"),
                        rs.getString("shape"),
                        rs.getString("data"));
            } else {
                throw new SQLException("Job " + jobId + " not found");
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Cannot read results for job " + jobId, e);
            throw new DBException(e);
        }
    }

    public void saveResults(String results, ResultsFormat resultsFormat)
            throws DBException {

        String jobId = System.getProperty("JOB_ID", System.getenv("JOB_ID"));
        String executionNode = System.getenv("NODE");
        String function = System.getenv().getOrDefault("FUNCTION", "JAVA");

        saveResults(results, resultsFormat, jobId, executionNode, function);
    }

    public void saveResults(String results, ResultsFormat resultsFormat, String jobId, String executionNode, String function)
            throws DBException {

        try (Connection conn = getConnection()) {

            String insertRequest = String.format("INSERT INTO %s (job_id, node, data, shape, function) VALUES (?, ?, ?, ?, ?)", outTable);

            try (PreparedStatement stmt = conn.prepareStatement(insertRequest)) {
                stmt.setString(1, jobId);
                stmt.setString(2, executionNode);
                stmt.setString(3, results);
                stmt.setString(4, resultsFormat.getShape());
                stmt.setString(5, function);

                stmt.executeUpdate();
                conn.commit();
            }
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    public static class JobResults {

        private String node;
        private ResultsFormat resultsFormat;
        private String results;

        JobResults(String executionNode, String shape, String results) {
            this.node = executionNode;
            this.resultsFormat = shapeToResultsFormat(shape);
            this.results = results;
        }

        public String getNode() {
            return node;
        }

        public ResultsFormat getResultsFormat() {
            return resultsFormat;
        }

        public String getResults() {
            return results;
        }
    }

    public enum ResultsFormat {
        PFA_JSON("pfa_json", "application/pfa+json"),
        PFA_YAML("pfa_yaml", "application/pfa+yaml"),
        HIGHCHARTS("highcharts_json", "application/highcharts+json"),
        JAVASCRIPT_VISJS("visjs", "application/visjs+javascript"),
        PNG_IMAGE("png_image", "image/png;base64"),
        SVG_IMAGE("svg_image", "image/svg+xml");

        private final String shape;
        private final String mimeType;

        ResultsFormat(String shape, String mimeType) {
            this.shape = shape;
            this.mimeType = mimeType;
        }

        public String getShape() {
            return shape;
        }

        public String getMimeType() {
            return mimeType;
        }

    }

    private static ResultsFormat shapeToResultsFormat(String shape) {
        for (ResultsFormat format: ResultsFormat.values()) {
            if (format.getShape().equals(shape)) {
                return format;
            }
        }
        throw new IllegalArgumentException("Unknown shape: " + shape);
    }
}
