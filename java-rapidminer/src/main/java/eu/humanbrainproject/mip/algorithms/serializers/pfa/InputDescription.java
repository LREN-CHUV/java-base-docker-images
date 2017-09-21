package eu.humanbrainproject.mip.algorithms.serializers.pfa;

import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;
import java.util.List;

public abstract class InputDescription {

    public void writePfaInput(JsonGenerator jgen) throws IOException {

        // Input
        jgen.writeObjectFieldStart("input");
        jgen.writeStringField("doc", "Input is the list of covariables and groups");
        jgen.writeStringField("name", "DependentVariables");
        jgen.writeStringField("type", "record");
        jgen.writeArrayFieldStart("fields");
        for (String covariable : getCovariables()) {
            jgen.writeStartObject();
            jgen.writeStringField("name", covariable);
            switch (getType(covariable)) {
                case REAL: {
                    jgen.writeStringField("type", "double");
                    break;
                }
                case CATEGORICAL: {
                    jgen.writeObjectFieldStart("type");
                    jgen.writeStringField("type", "enum");
                    jgen.writeStringField("name", "Enum_" + covariable);
                    jgen.writeArrayFieldStart("symbols");
                    for (String symbol : getCategoricalValues(covariable)) {
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

    public void writePfaOutput(JsonGenerator jgen) throws IOException {

        // Output
        jgen.writeObjectFieldStart("output");
        jgen.writeStringField("doc", "Output is the estimate of the variable");

        if (getVariables().length == 1) {
            switch (getType(getVariables()[0])) {
                case REAL: {
                    jgen.writeStringField("type", "double");
                    break;
                }
                case CATEGORICAL: {
                    // Convert categorial output to a string
                    jgen.writeStringField("type", "string");
                    break;
                }
            }
        } else {
            jgen.writeStringField("type", "record");
            jgen.writeArrayFieldStart("fields");
            for (String variable : getVariables()) {
                jgen.writeStartObject();
                jgen.writeStringField("name", variable);
                switch (getType(variable)) {
                    case REAL: {
                        jgen.writeStringField("type", "double");
                        break;
                    }
                    case CATEGORICAL: {
                        // Convert categorial output to a string
                        jgen.writeStringField("type", "string");
                        break;
                    }
                }
                jgen.writeEndObject();
            }
            jgen.writeEndArray();
        }
        jgen.writeEndObject();
    }

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
                jgen.writeStringField("variable", this.getVariables()[0]);
                jgen.writeObjectField("covariables", this.getCovariables());
                jgen.writeStringField("sql", getQuery());
                jgen.writeNumberField("count", getDataSize());
            }
            jgen.writeEndObject();

        }
        jgen.writeEndObject();
    }

    protected abstract VariableType getType(String variable);

    protected abstract List<String> getCategoricalValues(String variable);

    protected abstract String getQuery();

    protected abstract int getDataSize();

    protected abstract String[] getVariables();

    protected abstract String[] getCovariables();

    public enum VariableType {
        REAL,
        CATEGORICAL
    }

}
