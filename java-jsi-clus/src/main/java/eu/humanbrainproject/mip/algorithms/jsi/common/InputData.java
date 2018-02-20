
package eu.humanbrainproject.mip.algorithms.jsi.common;

import eu.humanbrainproject.mip.algorithms.Configuration;
import eu.humanbrainproject.mip.algorithms.db.DBException;
import eu.humanbrainproject.mip.algorithms.db.InputDataConnector;
import weka.core.Instances;
import weka.experiment.InstanceQuery;


public class InputData {

    private final InputDataConnector connector;
    private final String[] inputFeaturesNames;
    private final String[] outputFeaturesNames;
    private final int randomSeed;
    private Instances data;


    /**
     * @return the input data initialized from the environment variables
     */
    public static InputData fromEnv() throws DBException {
        final Configuration conf = Configuration.INSTANCE;

        /* targets */
        final String[] outputNames = conf.variables();
        
        /* inputs */
        final String[] inputNames = conf.covariables();
        
        /* random seed */
        Double myDouble = conf.randomSeed();
        Integer seed = (myDouble == null || myDouble < 0) ? 1 : Integer.valueOf((int) Math.round(myDouble));
        
        
        final InputDataConnector connector = InputDataConnector.fromEnv();

        return new InputData(inputNames, outputNames, connector, seed);
    }


    public InputData(String[] inputNames, String[] outputNames, InputDataConnector connector, int randomSeed) {
        this.inputFeaturesNames = inputNames;
        this.outputFeaturesNames = outputNames;
        this.randomSeed = randomSeed;
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


    public String[] getInputFeaturesNames() {
        return inputFeaturesNames;
    }


    /**
     * @return the name of the target variable
     */
    public String[] getOutputFeaturesNames() {
        return outputFeaturesNames;
    }


    /**
     * @return the SQL query
     */
    public String getQuery() {
        if (connector == null) {
            return "NO QUERY";
        }
        else {
            return connector.getQuery();
        }
    }


    /**
     * @return the random seed
     */
    public int getRandomSeed() {
        return randomSeed;
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

                return instances;

            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
