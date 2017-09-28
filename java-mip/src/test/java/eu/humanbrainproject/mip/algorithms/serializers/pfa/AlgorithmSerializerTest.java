package eu.humanbrainproject.mip.algorithms.serializers.pfa;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.opendatagroup.hadrian.jvmcompiler.PFAEngine;
import com.opendatagroup.hadrian.jvmcompiler.PFAEngine$;
import eu.humanbrainproject.mip.algorithms.SimpleAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import scala.Option;
import scala.collection.immutable.HashMap;

import java.io.IOException;

class AlgorithmSerializerTest {

    private AlgorithmSerializer<SimpleAlgorithm> serializer;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {

        serializer = new AlgorithmSerializer<SimpleAlgorithm>() {
            @Override
            protected InputDescription getInputDescription(SimpleAlgorithm value) {
                return new MixedInputDescription(value);
            }

            @Override
            protected void writeModelConstants(SimpleAlgorithm value, JsonGenerator jgen) throws IOException {
                jgen.writeObjectFieldStart("model");
                {
                    jgen.writeStringField("type", "int");
                    jgen.writeNumberField("init", 42);
                }
                jgen.writeEndObject();
            }
        };

        objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule("Weka", new Version(1, 0, 0, null, null, null));
        module.addSerializer(SimpleAlgorithm.class, serializer);
        objectMapper.registerModule(module);
    }

    @Test
    void serializerTest() throws Exception {
        SimpleAlgorithm algorithm = new SimpleAlgorithm();
        String pfa = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(algorithm);

        System.out.println(pfa);

        getPFAEngine(pfa);
    }

    private PFAEngine<Object, Object> getPFAEngine(String pfa) {
        return PFAEngine$.MODULE$.fromJson(pfa, new HashMap<>(), "0.8.1", Option.empty(),
                1, Option.empty(), false).head();
    }

}