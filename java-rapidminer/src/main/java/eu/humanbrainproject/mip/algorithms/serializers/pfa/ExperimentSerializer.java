package eu.humanbrainproject.mip.algorithms.serializers.pfa;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import eu.humanbrainproject.mip.algorithms.Configuration;
import eu.humanbrainproject.mip.algorithms.Experiment;

import java.io.IOException;

public abstract class ExperimentSerializer<T extends Experiment> extends JsonSerializer<T> {

    @Override
    public void serialize(T value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException {

        jgen.writeStartObject();
        {
            jgen.writeStringField("name", value.getName());
            jgen.writeStringField("doc", value.getDocumentation());

            // Metadata
            jgen.writeFieldName("metadata");
            jgen.writeStartObject();
            jgen.writeStringField("docker_image", Configuration.INSTANCE.dockerImage());
        }
        jgen.writeEndObject();

        InputDescription inputDescription = getInputDescription(value);

        if (inputDescription != null) {
            inputDescription.writePfaInput(jgen);
            inputDescription.writePfaOutput(jgen);
        }

        // Cells
        jgen.writeFieldName("cells");
        jgen.writeStartObject();
        {
            if (inputDescription != null) {
                inputDescription.writeQuery(jgen);
            }
            writeError(value, jgen);
            writeModel(value, jgen);
        }
        jgen.writeEndObject();
    }

    protected abstract InputDescription getInputDescription(T value);
    protected abstract void writeError(T value, JsonGenerator jgen) throws IOException;
    protected abstract void writeModel(T value, JsonGenerator jgen) throws IOException;

}
