package eu.humanbrainproject.mip.algorithms.serializers.pfa;

import com.fasterxml.jackson.core.JsonGenerator;
import eu.humanbrainproject.mip.algorithms.SimpleAlgorithm;

import java.io.IOException;

class SimpleAlgorithmSerializer extends AlgorithmSerializer<SimpleAlgorithm> {
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
}
