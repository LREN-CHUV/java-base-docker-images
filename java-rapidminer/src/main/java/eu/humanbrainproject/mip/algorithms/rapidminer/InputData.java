package eu.humanbrainproject.mip.algorithms.rapidminer;

import java.io.IOException;
import java.util.*;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import com.fasterxml.jackson.core.JsonGenerator;

import com.rapidminer.example.Attribute;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.example.table.*;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.tools.Ontology;

import eu.humanbrainproject.mip.algorithms.rapidminer.db.DBConnectionDescriptor;
import eu.humanbrainproject.mip.algorithms.rapidminer.db.DBConnector;
import eu.humanbrainproject.mip.algorithms.rapidminer.db.DBException;
import eu.humanbrainproject.mip.algorithms.rapidminer.db.InputDataConnector;
import eu.humanbrainproject.mip.algorithms.rapidminer.exceptions.RapidMinerException;


/**
 * @author Arnaud Jutzeler
 */
public class InputData {

    private final InputDataConnector connector;
    private final String[] featuresNames;
    private final String variableName;

    //protected Map<String, Integer> types;

    protected final ExampleSet data;

    /**
     * @return
     */
    public static InputData fromEnv() throws DBException, RapidMinerException {
        // Read first system property then env variables
        final String labelName = System.getProperty("PARAM_variables", System.getenv("PARAM_variables"));
        final String[] featuresNames = System.getProperty("PARAM_covariables", System.getenv("PARAM_covariables")).split(",");
        final String query = System.getProperty("PARAM_query", System.getenv().getOrDefault("PARAM_query", ""));
        final InputDataConnector connector = new InputDataConnector(query, DBConnectionDescriptor.inputConnector());

        return new InputData(featuresNames, labelName, connector);
    }

    public InputData(String[] featuresNames, String variableName, InputDataConnector connector) throws DBException, RapidMinerException {
        this.featuresNames = featuresNames;
        this.variableName = variableName;
        //this.types = new HashMap<>();

        this.connector = connector;
        this.data = createExampleSet();
    }

    /**
     * Return the relevant data structure to pass as input to RapidMiner
     *
     * @return
     */
    public ExampleSet getData() {
        return data;
    }

    public InputDataConnector getConnector() {
        return connector;
    }

    public String[] getFeaturesNames() {
        return featuresNames;
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

    /**
     * Get the data from DB
     *
     * @return
     * @throws DBException
     */
    private ExampleSet createExampleSet() throws RapidMinerException, DBException {

        DBConnector connector = new DBConnector(query, DBConnector.Direction.DATA_IN);
        ResultSet rs = connector.connect();

        // Create attribute list
        ResultSetMetaData rsmd;
        List<Attribute> attributes = new ArrayList<>();
        try {
            rsmd = rs.getMetaData();
            for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                String name = rsmd.getColumnName(i);
                String typeName = rsmd.getColumnTypeName(i);

                int type = getOntology(typeName);
                //types.put(name, type);
                attributes.add(AttributeFactory.createAttribute(name, type));
            }
        } catch (SQLException e) {
            throw new DBException(e);
        }

        // Create table
        MemoryExampleTable table = new MemoryExampleTable(attributes);

        ResultSetDataRowReader reader = new ResultSetDataRowReader(new DataRowFactory(DataRowFactory.TYPE_DOUBLE_ARRAY, '.'), attributes, rs);
        while (reader.hasNext()) {
            table.addDataRow(reader.next());
        }

        connector.disconnect();


        // Create example set
        try {
            return table.createExampleSet(table.findAttribute(variableName));
        } catch (OperatorException e) {
            throw new RapidMinerException(e);
        }
    }

    /**
     * @param jgen
     * @throws IOException
     */
    public void writeInput(JsonGenerator jgen) throws IOException {

        // Input
        jgen.writeObjectFieldStart("input");
        jgen.writeStringField("doc", "Input is the list of covariables and groups");
        jgen.writeStringField("name", "DependentVariables");
        jgen.writeStringField("type", "record");
        jgen.writeArrayFieldStart("fields");
        for (String featureName : featuresNames) {
            jgen.writeStartObject();
            jgen.writeStringField("name", featureName);
            switch (getType(featureName)) {
                case Ontology.REAL: {
                    jgen.writeStringField("type", "double");
                    break;
                }
                case Ontology.NOMINAL: {
                    jgen.writeObjectFieldStart("type");
                    jgen.writeStringField("type", "enum");
                    jgen.writeStringField("name", "Enum_" + featureName);
                    jgen.writeArrayFieldStart("symbols");
                    for (String symbol : getSymbols(featureName)) {
                        jgen.writeString(symbol);
                    }
                    jgen.writeEndArray();
                    jgen.writeEndObject();
                    break;
                }
            }
            jgen.writeEndObject();
        }
        jgen.writeEndArray();
        jgen.writeEndObject();
    }

    /**
     * @param jgen
     * @throws IOException
     */
    public void writeOutput(JsonGenerator jgen) throws IOException {

        // Output
        jgen.writeObjectFieldStart("output");
        jgen.writeStringField("doc", "Output is the estimate of the variable");
        switch (getType(variableName)) {
            case Ontology.REAL: {
                jgen.writeStringField("type", "double");
                break;
            }
            case Ontology.NOMINAL: {
                jgen.writeStringField("type", "string");
            /*jgen.writeObjectFieldStart("type");
				jgen.writeStringField("type", "enum");
				jgen.writeStringField("name", "VariableType");
				jgen.writeArrayFieldStart("symbols");
				for (String symbol : getSymbols(variableName)) {
					jgen.writeString(symbol);
				}
				jgen.writeEndArray();
			jgen.writeEndObject();*/
                break;
            }
        }
        jgen.writeEndObject();
    }

    /**
     * @param jgen
     * @throws IOException
     */
    public void writeQuery(JsonGenerator jgen) throws IOException {

        // Query
        jgen.writeObjectFieldStart("query");
        {
            // Type definition of the query
            jgen.writeObjectFieldStart("type");
            {
                jgen.writeStringField("doc", "Definition of the query that has produced this model");
                jgen.writeStringField("name", "Query");
                jgen.writeStringField("type", "record");

                // List of fields for Query type
                jgen.writeArrayFieldStart("fields");
                {

                    // Variable
                    jgen.writeStartObject();
                    {
                        jgen.writeStringField("doc", "Dependent variable");
                        jgen.writeStringField("name", "variable");
                        jgen.writeStringField("type", "string");
                    }
                    jgen.writeEndObject();

                    // Covariables
                    jgen.writeStartObject();
                    {
                        jgen.writeStringField("doc", "List of explanatory variables");
                        jgen.writeStringField("name", "covariables");
                        jgen.writeObjectFieldStart("type");
                        {
                            jgen.writeStringField("type", "array");
                            jgen.writeObjectFieldStart("items");
                            {
                                jgen.writeStringField("type", "string");
                            }
                            jgen.writeEndObject();
                        }
                        jgen.writeEndObject();
                    }
                    jgen.writeEndObject();

                    // SQL
                    jgen.writeStartObject();
                    {
                        jgen.writeStringField("doc", "SQL query");
                        jgen.writeStringField("name", "sql");
                        jgen.writeStringField("type", "string");
                    }
                    jgen.writeEndObject();

                    // Count
                    jgen.writeStartObject();
                    {
                        jgen.writeStringField("doc", "Number of records selected by the query");
                        jgen.writeStringField("name", "count");
                        jgen.writeStringField("type", "int");
                    }
                    jgen.writeEndObject();
                }
                jgen.writeEndArray();

            }
            jgen.writeEndObject();

            // Init - value of the query object
            jgen.writeObjectFieldStart("init");
            {
                jgen.writeStringField("variable", this.variableName);
                jgen.writeObjectField("covariables", this.featuresNames);
                jgen.writeStringField("sql", this.query);
                jgen.writeNumberField("count", this.data.size());
            }
            jgen.writeEndObject();

        }
        jgen.writeEndObject();
    }

    /**
     * @param name
     * @return
     */
    private int getType(String name) {
        return data.getAttributes().get(name).getValueType();
    }

    /**
     * @param name
     * @return
     */
    private List<String> getSymbols(String name) {
        try {
            return this.data.getAttributes().get(name).getMapping().getValues();
        } catch (UnsupportedOperationException e) {
            return new LinkedList<>();
        }
    }

    /**
     * @return
     */
    public String getVariableName() {
        return variableName;
    }

    /**
     * @return
     */
    public String getQuery() {
        return query;
    }
}