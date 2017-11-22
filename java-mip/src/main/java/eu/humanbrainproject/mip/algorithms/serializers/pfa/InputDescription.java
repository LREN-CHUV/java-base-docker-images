package eu.humanbrainproject.mip.algorithms.serializers.pfa;

import com.fasterxml.jackson.core.JsonGenerator;
import eu.humanbrainproject.mip.algorithms.Algorithm;
import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static eu.humanbrainproject.mip.algorithms.Algorithm.AlgorithmCapability.INPUT_DATA_MISSING_VALUES;

/**
 * Helper to describe algorithm inputs and outputs, including the query used during learning.
 */
public abstract class InputDescription<T extends Algorithm> {

    private final T algorithm;

    public InputDescription(T algorithm) {
        this.algorithm = algorithm;
    }

    protected T getAlgorithm() {
        return algorithm;
    }

    public void writeInputMetadata(JsonGenerator jgen) throws IOException {
        boolean nullableField = algorithm.getCapabilities().contains(INPUT_DATA_MISSING_VALUES);
        jgen.writeStringField("accepts_missing_values", String.valueOf(nullableField));
    }

    public void writePfaInput(JsonGenerator jgen) throws Exception {

        // Input
        jgen.writeObjectFieldStart("input");
        jgen.writeStringField("doc", "Input is the list of covariables and groups");
        jgen.writeStringField("name", "DependentVariables");
        jgen.writeStringField("type", "record");
        jgen.writeArrayFieldStart("fields");
        for (String covariable : getCovariables()) {
            jgen.writeStartObject();
            jgen.writeStringField("name", covariable);
            writeInputFieldType(jgen, covariable);
            jgen.writeEndObject();
        }
        jgen.writeEndArray();
        jgen.writeEndObject();
    }

    protected void writeInputFieldType(JsonGenerator jgen, String covariable) throws Exception {
        final VariableType type = getType(covariable);
        boolean nullableField = algorithm.getCapabilities().contains(INPUT_DATA_MISSING_VALUES);
        Schema schema;
        switch (type) {
            case REAL: {
                if (nullableField) {
                    schema = SchemaBuilder.builder().unionOf().doubleType().and().nullType().endUnion();
                } else {
                    schema = SchemaBuilder.builder().doubleType();
                }
                break;
            }
            case CATEGORICAL_STRING: {
                List<String> categoricalValues = getCategoricalValues(covariable);
                String[] symbols = asSymbols(categoricalValues);
                Schema enumType = SchemaBuilder.builder().enumeration("Enum_" + covariable).symbols(symbols);
                if (nullableField) {
                    schema = SchemaBuilder.unionOf().type(enumType).and().intType().and().nullType().endUnion();
                } else {
                    schema = enumType;
                }
                break;
            }
            case CATEGORICAL_INT: {
                List<String> categoricalValues = getCategoricalValues(covariable);
                String[] symbols = asSymbols(categoricalValues);
                Schema enumType = SchemaBuilder.builder().enumeration("Enum_" + covariable).symbols(symbols);
                if (nullableField) {
                    schema = SchemaBuilder.unionOf().type(enumType).and().intType().and().nullType().endUnion();
                } else {
                    schema = SchemaBuilder.unionOf().type(enumType).and().intType().endUnion();
                }
                break;
            }
            default:
                throw new IllegalArgumentException("Unknown type: " + type);
        }

        jgen.writeFieldName("type");
        jgen.writeRawValue(schema.toString());
    }

    protected String[] asSymbols(List<String> values) {
        List<String> symbols = values.stream()
                .map(v -> v.matches("^\\d.*") ? "_" + v : v)
                .map(v -> v.replaceAll("[\\s'\"]", "_"))
                .collect(Collectors.toList());
        return symbols.toArray(new String[symbols.size()]);
    }

    public void writePfaOutput(JsonGenerator jgen) throws Exception {

        // Output
        jgen.writeObjectFieldStart("output");
        jgen.writeStringField("doc", "Output is the estimate of the variable");

        if (getVariables().length == 1) {
            final VariableType type = getType(getVariables()[0]);
            jgen.writeStringField("type", toPFAType(type));
        } else {
            jgen.writeStringField("type", "record");
            jgen.writeArrayFieldStart("fields");
            for (String variable : getVariables()) {
                jgen.writeStartObject();
                jgen.writeStringField("name", variable);
                final VariableType type = getType(variable);
                jgen.writeStringField("type", toPFAType(type));
                jgen.writeEndObject();
            }
            jgen.writeEndArray();
        }
        jgen.writeEndObject();
    }

    public void writeQuery(JsonGenerator jgen) throws Exception {

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
                jgen.writeStringField("variable", this.getVariables()[0]);
                jgen.writeObjectField("covariables", this.getCovariables());
                jgen.writeStringField("sql", getQuery());
                jgen.writeNumberField("count", getDataSize());
            }
            jgen.writeEndObject();

        }
        jgen.writeEndObject();
    }

    public void writeInputToLocalVars(JsonGenerator jgen) {
    }

    protected List<String> getCategoricalValues(String variable) throws Exception {
        return Collections.emptyList();
    }

    protected abstract VariableType getType(String variable) throws Exception;

    protected abstract String getQuery();

    protected abstract int getDataSize() throws Exception;

    protected abstract String[] getVariables();

    protected abstract String[] getCovariables();

    public enum VariableType {
        REAL,
        CATEGORICAL_STRING,
        CATEGORICAL_INT
    }

    public static String toPFAType(VariableType variableType) {
        switch (variableType) {
            case REAL: return "double";
            case CATEGORICAL_STRING: return "string";
            case CATEGORICAL_INT: return "int";
            default: throw new IllegalArgumentException("Unknown type: " + variableType);
        }
    }

}
