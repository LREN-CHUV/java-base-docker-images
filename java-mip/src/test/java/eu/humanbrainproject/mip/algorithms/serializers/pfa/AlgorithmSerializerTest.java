package eu.humanbrainproject.mip.algorithms.serializers.pfa;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.opendatagroup.hadrian.jvmcompiler.PFAEngine;
import com.opendatagroup.hadrian.jvmcompiler.PFAEngine$;
import eu.humanbrainproject.mip.algorithms.NullableInputAlgorithm;
import eu.humanbrainproject.mip.algorithms.SimpleAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import scala.Option;
import scala.collection.immutable.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("When serializing an algorithm, AlgorithmSerializer produces")
class AlgorithmSerializerTest {
    private static ObjectMapper mapper = new ObjectMapper();

    private AlgorithmSerializer<SimpleAlgorithm> simpleSerializer;
    private NullableInputAlgorithmSerializer nullableInputSerializer;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {

        simpleSerializer = new SimpleAlgorithmSerializer();
        nullableInputSerializer = new NullableInputAlgorithmSerializer();

        objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule("Weka", new Version(1, 0, 0, null, null, null));
        module.addSerializer(SimpleAlgorithm.class, simpleSerializer);
        module.addSerializer(NullableInputAlgorithm.class, nullableInputSerializer);
        objectMapper.registerModule(module);
    }

    @Test
    @DisplayName("a valid PFA document for a predictive algorithm that does nothing")
    void serializeEmptyPredictiveAlgorithmTest() throws Exception {
        SimpleAlgorithm algorithm = new SimpleAlgorithm();
        String pfa = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(algorithm);

        assertNotNull(getPFAEngine(pfa));

        final JsonNode jsonPfa = mapper.readTree(pfa);
        final JsonNode jsonExpected = mapper.readTree(getClass().getResource("simple_algo.pfa.json"));

        assertEquals(jsonExpected, jsonPfa);
    }

    @Test
    @DisplayName("a valid PFA document for an algorithm that failed during training")
    void serializeFailingAlgorithmTest() throws Exception {
        SimpleAlgorithm algorithm = new SimpleAlgorithm();
        algorithm.setErrorMessage("Training failed");

        String pfa = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(algorithm);

        assertNotNull(getPFAEngine(pfa));

        final JsonNode jsonPfa = mapper.readTree(pfa);
        final JsonNode jsonExpected = mapper.readTree(getClass().getResource("failing_algorithm.pfa.json"));

        assertEquals(jsonExpected, jsonPfa);
    }

    @Test
    @DisplayName("a valid PFA document supporting nullable inputs for algorithms with this capability")
    void serializeAlgorithmSupportingNullableInputTest() throws Exception {
        NullableInputAlgorithm algorithm = new NullableInputAlgorithm();
        String pfa = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(algorithm);

        assertNotNull(getPFAEngine(pfa));

        final JsonNode jsonPfa = mapper.readTree(pfa);
        final JsonNode jsonExpected = mapper.readTree(getClass().getResource("nullable_input_algorithm.pfa.json"));

        assertEquals(jsonExpected, jsonPfa);
    }


    private PFAEngine<Object, Object> getPFAEngine(String pfa) {
        return PFAEngine$.MODULE$.fromJson(pfa, new HashMap<>(), "0.8.1", Option.empty(),
                1, Option.empty(), false).head();
    }

}