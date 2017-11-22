package eu.humanbrainproject.mip.algorithms.chaos;

import java.util.*;

import eu.humanbrainproject.mip.algorithms.Configuration;
import eu.humanbrainproject.mip.algorithms.db.DBException;
import eu.humanbrainproject.mip.algorithms.db.InputDataConnector;


/**
 * @author Ludovic Claude
 */
public class InputData {

    private final InputDataConnector connector;
    private final String[] featuresNames;
    private final String variableName;
    private List<Map<String, Double>> data;

    /**
     * @return the input data initialised from the environment variables
     */
    public static InputData fromEnv() throws DBException {
        final Configuration conf = Configuration.INSTANCE;

        // Read first system property then env variables
        final String labelName = conf.variables()[0];
        final String[] featuresNames = conf.covariables();

        final InputDataConnector connector = InputDataConnector.fromEnv();

        return new InputData(featuresNames, labelName, connector);
    }

    public InputData(String[] featuresNames, String variableName, InputDataConnector connector) {
        this.featuresNames = featuresNames;
        this.variableName = variableName;
        //this.types = new HashMap<>();

        this.connector = connector;
    }

    /**
     * Return some sample input data
     *
     * @return the input data
     */
    public List<Map<String, Double>> getData() throws DBException {
        if (data == null) {
            data = new ArrayList<>();
            Map<String, Double> row1 = new HashMap<>();
            row1.put("col1", 1.0);
            row1.put("col2", 2.0);
            data.add(row1);
            Map<String, Double> row2 = new HashMap<>();
            row2.put("col1", 1.1);
            row2.put("col2", 2.2);
            data.add(row2);
        }
        return data;
    }

    public String[] getFeaturesNames() {
        return featuresNames;
    }

    /**
     * @return the name of the target variable
     */
    public String getVariableName() {
        return variableName;
    }

    /**
     * @return the SQL query
     */
    public String getQuery() {
        if (connector == null) {
            return "NO QUERY";
        } else {
            return connector.getQuery();
        }
    }

}
