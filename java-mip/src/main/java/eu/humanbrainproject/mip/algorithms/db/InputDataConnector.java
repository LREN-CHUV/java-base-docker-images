package eu.humanbrainproject.mip.algorithms.db;

import eu.humanbrainproject.mip.algorithms.Configuration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Function;

public class InputDataConnector extends DBConnector {

    /**
     * @return the input data connector defined by the configuration
     */
    public static InputDataConnector fromEnv() {
        final Configuration conf = Configuration.INSTANCE;
        return new InputDataConnector(
                conf.inputSqlQuery(),
                conf.randomSeed(),
                DBConnectionDescriptor.inputConnectorFromEnv());
    }

    private final String query;
    private final double seed;

    public InputDataConnector(String query, double seed, DBConnectionDescriptor dbConnectionDescriptor) {
        super(dbConnectionDescriptor);
        this.query = query;
        this.seed = seed;
    }

    public String getQuery() {
        return query;
    }

    public <M> M fetchData(Function<ResultSet, M> transform) throws DBException {
        return select(getQuery(), transform);
    }

    protected void beforeSelect(Connection conn) throws SQLException {
        conn.setAutoCommit(false);
        PreparedStatement preparedStatement = conn.prepareStatement("SELECT setseed(?)");
        preparedStatement.setDouble(1, seed);
        preparedStatement.execute();
    }

    protected void afterSelect(Connection conn) throws SQLException {
        conn.commit();
    }

}
