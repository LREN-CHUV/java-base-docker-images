package eu.humanbrainproject.mip.algorithms.serializers.pfa;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.humanbrainproject.mip.algorithms.NullableInputAlgorithm;
import eu.humanbrainproject.mip.algorithms.SimpleAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("InputDescription should")
public class InputDescriptionTest {

    private static final SimpleAlgorithm SIMPLE_ALGORITHM = new SimpleAlgorithm();
    private static final NullableInputAlgorithm NULLABLE_INPUT_ALGORITHM = new NullableInputAlgorithm();
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private StringWriter writer;
    private JsonGenerator generator;

    @BeforeEach
    void setUp() throws Exception {
        JsonFactory factory = new JsonFactory();
        writer = new StringWriter();
        generator = factory.createGenerator(writer);
        generator.setCodec(new ObjectMapper());
    }

    @Test
    @DisplayName("describe the query used during the training of the model")
    public void testWriteQuery() throws Exception {

        NumericalInputDescription inputDescription = new NumericalInputDescription(SIMPLE_ALGORITHM);

        generator.writeStartObject();
        inputDescription.writeQuery(generator);
        generator.writeEndObject();
        generator.close();

        assertJsonEquals(writer.toString(), "input_query.fragment.json");
    }

    @Test
    @DisplayName("indicate in the metadata that the predictive algorithm does not accepts missing values in the input data")
    public void testWriteInputMetadataNoMissingValues() throws Exception {

        InputDescription inputDescription = new NumericalInputDescription(SIMPLE_ALGORITHM);

        generator.writeStartObject();
        inputDescription.writeInputMetadata(generator);
        generator.writeEndObject();
        generator.close();

        assertJsonEquals(writer.toString(), "does_not_accept_missing_values.fragment.json");
    }

    @Test
    @DisplayName("indicate in the metadata that the predictive algorithm supports missing values in the input data")
    public void testWriteInputMetadataSupportsMissingValues() throws Exception {

        InputDescription inputDescription = new NullableInputDescription(NULLABLE_INPUT_ALGORITHM);

        generator.writeStartObject();
        inputDescription.writeInputMetadata(generator);
        generator.writeEndObject();
        generator.close();

        assertJsonEquals(writer.toString(), "accepts_missing_values.fragment.json");
    }

    @Test
    @DisplayName("describe the type of the output for single output values")
    public void testWritePfaOutput() throws Exception {

        InputDescription inputDescription = new NumericalInputDescription(SIMPLE_ALGORITHM);

        generator.writeStartObject();
        inputDescription.writePfaOutput(generator);
        generator.writeEndObject();
        generator.close();

        assertJsonEquals(writer.toString(), "single_output.fragment.json");
    }

    private void assertJsonEquals(String jsonDocument, String pathToExpected) throws IOException {
        final JsonNode jsonTest = MAPPER.readTree(jsonDocument);
        final JsonNode jsonExpected = MAPPER.readTree(getClass().getResource(pathToExpected));

        assertEquals(jsonExpected, jsonTest);
    }

}
