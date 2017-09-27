package eu.humanbrainproject.mip.algorithms.weka;

import eu.humanbrainproject.mip.algorithms.Configuration;
import eu.humanbrainproject.mip.algorithms.db.DBException;
import eu.humanbrainproject.mip.algorithms.db.InputDataConnector;
import weka.core.Instances;
import weka.experiment.InstanceQuery;


public class InputData {

    private final InputDataConnector connector;
    private final String[] featuresNames;
    private final String variableName;
    private Instances data;

    //protected Map<String, Integer> types;

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
     * Return the relevant data structure to pass as input to Weka
     *
     * @return the input data as an Instances to train Weka algorithms
     */
    public Instances getData() throws DBException {
        if (data == null) {
           data = createInstances();
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

    /**
     * Get the data from DB
     */
    protected Instances createInstances() throws DBException {

        return connector.fetchData(resultSet -> {

            try {
                InstanceQuery instanceQuery = new InstanceQuery();
                instanceQuery.setQuery(getQuery());
                final Instances instances = InstanceQuery.retrieveInstances(instanceQuery, resultSet);
                final int classIndex = instances.attribute(getVariableName()).index();
                instances.setClassIndex(classIndex);
                return instances;

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        });
    }
}
