package eu.humanbrainproject.mip.algorithms.serializers.pfa;

import com.fasterxml.jackson.core.JsonGenerator;
import eu.humanbrainproject.mip.algorithms.NullableInputAlgorithm;

import java.io.IOException;

class NullableInputAlgorithmSerializer extends AlgorithmSerializer<NullableInputAlgorithm> {
    @Override
    protected InputDescription getInputDescription(NullableInputAlgorithm value) {
        return new NullableInputDescription(value);
    }

    @Override
    protected void writeModelConstants(NullableInputAlgorithm value, JsonGenerator jgen) throws IOException {
        jgen.writeObjectFieldStart("model");
        {
            jgen.writeStringField("type", "int");
            jgen.writeNumberField("init", 42);
        }
        jgen.writeEndObject();
    }
}
