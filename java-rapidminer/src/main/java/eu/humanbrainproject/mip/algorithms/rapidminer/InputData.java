package eu.humanbrainproject.mip.algorithms.rapidminer;

import java.util.*;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import com.rapidminer.example.Attribute;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.example.table.*;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.tools.Ontology;

import eu.humanbrainproject.mip.algorithms.Configuration;
import eu.humanbrainproject.mip.algorithms.db.DBException;
import eu.humanbrainproject.mip.algorithms.db.InputDataConnector;
import eu.humanbrainproject.mip.algorithms.rapidminer.exceptions.RapidMinerException;


/**
 * @author Arnaud Jutzeler
 */
public class InputData {

    private final InputDataConnector connector;
    private final String[] featuresNames;
    private final String variableName;
    private ExampleSet data;

    //protected Map<String, Integer> types;

    /**
     * @return the input data initialised from the environment variables
     */
    public static InputData fromEnv() throws DBException, RapidMinerException {
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
     * Return the relevant data structure to pass as input to RapidMiner
     *
     * @return the input data as an ExampleSet to train RapidMiner algorithms
     */
    public ExampleSet getData() throws DBException, RapidMinerException {
        if (data == null) {
           data = createExampleSet();
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
            return "";
        } else {
            return connector.getQuery();
        }
    }

    /**
     * Get the data from DB
     */
    protected ExampleSet createExampleSet() throws DBException {

        return connector.fetchData(resultSet -> {
            try {

                // Create attribute list
                ResultSetMetaData rsmd = resultSet.getMetaData();
                List<Attribute> attributes = new ArrayList<>();

                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    String name = rsmd.getColumnName(i);
                    String typeName = rsmd.getColumnTypeName(i);

                    int type = getOntology(typeName);
                    //types.put(name, type);
                    attributes.add(AttributeFactory.createAttribute(name, type));
                }

                // Create table
                MemoryExampleTable table = new MemoryExampleTable(attributes);

                DataRowFactory dataRowFactory = new DataRowFactory(DataRowFactory.TYPE_DOUBLE_ARRAY, '.');
                ResultSetDataRowReader reader = new ResultSetDataRowReader(dataRowFactory, attributes, resultSet);
                while (reader.hasNext()) {
                    table.addDataRow(reader.next());
                }

                // Create example set
                return table.createExampleSet(table.findAttribute(variableName));

            } catch (SQLException | OperatorException e) {
                throw new RuntimeException(e);
            }

        });

    }

    /**
     * Match an Ontology to a DB type name
     *
     * @param typeName JDBC type name
     * @return
     */
    private static int getOntology(String typeName) {
        String[] real = {
                "numeric",
                "decimal",
                "tinyint",
                "smallint",
                "integer",
                "bigint",
                "real",
                "float",
                "double"
        };

        if (Arrays.asList(real).contains(typeName)) {
            return Ontology.REAL;
        } else {
            return Ontology.NOMINAL;
        }
    }

}