package eu.humanbrainproject.mip.algorithms.serializers.pfa;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import eu.humanbrainproject.mip.algorithms.Algorithm;
import eu.humanbrainproject.mip.algorithms.Configuration;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Serialize the algorithm after training to its PFA representation.
 *
 * @param <T> the class of the algorithm to serialize
 */
public abstract class AlgorithmSerializer<T extends Algorithm> extends JsonSerializer<T> {

    private static final Logger LOGGER = Logger.getLogger(AlgorithmSerializer.class.getName());

    @Override
    public void serialize(T value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException {

        jgen.writeStartObject();
        {
            jgen.writeStringField("name", value.getName());
            jgen.writeStringField("method", "map");
            jgen.writeStringField("doc", value.getDocumentation());

            String errorMessage = value.getErrorMessage();
            InputDescription inputDescription = getInputDescription(value);

            // Metadata
            jgen.writeObjectFieldStart("metadata");
            {
                writePfaMetadata(value, jgen);
                if (inputDescription != null) {
                    inputDescription.writeInputMetadata(jgen);
                }
            }
            jgen.writeEndObject();

            if (inputDescription != null) {
                try {
                    inputDescription.writePfaInput(jgen);
                    inputDescription.writePfaOutput(jgen);
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "Cannot generate PFA input or output", e);
                    errorMessage = e.getMessage();
                }
            } else {
                jgen.writeObjectFieldStart("input");
                jgen.writeEndObject();
                jgen.writeObjectFieldStart("output");
                jgen.writeEndObject();
            }

            // Cells
            jgen.writeObjectFieldStart("cells");
            {
                if (inputDescription != null) {
                    try {
                        inputDescription.writeQuery(jgen);
                    } catch (Exception e) {
                        LOGGER.log(Level.SEVERE, "Cannot generate PFA description for input query", e);
                        errorMessage = e.getMessage();
                    }
                }
                if (errorMessage != null) {
                    jgen.writeObjectFieldStart("error");
                    {
                        jgen.writeStringField("type", "string");
                        jgen.writeStringField("init", errorMessage);
                    }
                    jgen.writeEndObject();
                } else {
                    writeModelConstants(value, jgen);
                }
            }
            jgen.writeEndObject();

            if (errorMessage == null) {

                // Pools
                jgen.writeObjectFieldStart("pools");
                {
                    writePfaPools(value, jgen);
                }
                jgen.writeEndObject();

                // Begin
                jgen.writeArrayFieldStart("begin");
                {
                    writePfaBegin(value, jgen);
                }
                jgen.writeEndArray();

                // Action
                jgen.writeArrayFieldStart("action");
                {
                    if (inputDescription != null) {
                        inputDescription.writeInputToLocalVars(jgen);
                    }
                    writePfaAction(value, jgen);
                }
                jgen.writeEndArray();

                // End
                jgen.writeArrayFieldStart("end");
                {
                    writePfaEnd(value, jgen);
                }
                jgen.writeEndArray();

                // Functions
                jgen.writeObjectFieldStart("fcns");
                {
                    writePfaFunctionDefinitions(value, jgen);
                }
                jgen.writeEndObject();

            } else {
                // Action
                jgen.writeArrayFieldStart("action");
                {
                    writePfaErrorAction(value, jgen, errorMessage);

                }
                jgen.writeEndArray();
            }

        }
        jgen.writeEndObject();

    }

    protected abstract InputDescription getInputDescription(T value);

    /**
     * Write the model constants and optionally other data such as summary statistics in the 'cells' field of
     * the PFA document.
     */
    protected abstract void writeModelConstants(T value, JsonGenerator jgen) throws IOException;

    /**
     * Write a strings to strings mapping used to describe the scoring engine or its provenance.
     */
    protected void writePfaMetadata(T value, JsonGenerator jgen) throws IOException {
        jgen.writeStringField("docker_image", Configuration.INSTANCE.dockerImage());
    }

    /**
     * Write the expressions that are executed in the begin phase of the scoring engine’s run
     */
    protected void writePfaBegin(T value, JsonGenerator jgen) throws IOException {
        // Empty expression, to override if necessary
    }

    /**
     * Write the expression or JSON array of expressions that are executed for each input datum in
     * the active phase of the scoring engine’s run
     */
    protected void writePfaAction(T value, JsonGenerator jgen) throws IOException {
        jgen.writeStartObject();
        {
            jgen.writeStringField("error", "No action defined");
        }
        jgen.writeEndObject();
        jgen.writeStartObject();
        {
            InputDescription inputDescription = getInputDescription(value);
            generateDummyOutput(jgen, inputDescription);
        }
        jgen.writeEndObject();
    }

    /**
     * Write a PFA action that keeps failing to indicate that training of the model failed earlier on.
     */
    protected void writePfaErrorAction(T value, JsonGenerator jgen, String errorMessage) throws IOException {
        jgen.writeStartObject();
        {
            jgen.writeStringField("error", errorMessage);
        }
        jgen.writeEndObject();
        jgen.writeStartObject();
        {
            InputDescription inputDescription = getInputDescription(value);
            generateDummyOutput(jgen, inputDescription);
        }
        jgen.writeEndObject();
    }

    protected void generateDummyOutput(JsonGenerator jgen, InputDescription inputDescription) {
        try {
            switch (inputDescription.getType(inputDescription.getVariables()[0])) {
                case CATEGORICAL_STRING: {
                    jgen.writeStringField("string", "dummy");
                    break;
                }
                case CATEGORICAL_INT: {
                    jgen.writeNumberField("int", -1);
                    break;
                }
                case REAL: {
                    jgen.writeNumberField("double", -1.);
                    break;
                }
                default: {
                    jgen.writeStringField("string", "dummy");
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Cannot generate empty action", e);
        }
    }

    /**
     * Write the expressions that are executed in the end phase of the scoring engine’s run
     */
    protected void writePfaEnd(T value, JsonGenerator jgen) throws IOException {
        // Empty expression, to override if necessary
    }

    /**
     * Write the JSON object fields that are function definitions, defining routines that may
     * be called by expressions in begin, action, end, or by expressions in other functions.
     */
    protected void writePfaFunctionDefinitions(T value, JsonGenerator jgen) throws IOException {
        // Empty set of functions, to override if necessary
    }

    /**
     * Write the JSON object fields that specify dynamically allocated namespaces of typed persistent state.
     */
    protected void writePfaPools(T value, JsonGenerator jgen) throws IOException {
        // Empty set of pools, to override if necessary
    }
}
