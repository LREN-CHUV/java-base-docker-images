package eu.humanbrainproject.mip.algorithms.rapidminer.serializers.pfa;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import eu.humanbrainproject.mip.algorithms.rapidminer.InputData;
import eu.humanbrainproject.mip.algorithms.rapidminer.models.RapidMinerModel;
import eu.humanbrainproject.mip.algorithms.rapidminer.RapidMinerExperiment;


/**
 *
 * @author Arnaud Jutzeler
 */
public class RapidMinerExperimentSerializer extends JsonSerializer<RapidMinerExperiment> {

    @Override
    public void serialize(RapidMinerExperiment value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException {

        jgen.writeStartObject();
            jgen.writeStringField("name", value.name);
            jgen.writeStringField("doc", value.doc);

            // Metadata
            jgen.writeFieldName("metadata");
            jgen.writeStartObject();
            jgen.writeStringField("docker_image", value.docker_image);
            jgen.writeEndObject();

        InputData input = value.getInput();

        // Input, output
        if(input != null) {
            input.writeInput(jgen);
            input.writeOutput(jgen);
        }

        // Cells
        jgen.writeFieldName("cells");
        jgen.writeStartObject();

        // Query
        if(input != null) {
            input.writeQuery(jgen);
        }

        // Error
        if(value.exception != null) {
            jgen.writeStringField("error", value.exception.getMessage());
        }

        RapidMinerModel model = value.getModel();

        // Model representation
        if(model != null) {
            model.writeRepresentation(jgen);
        }

        jgen.writeEndObject();
    }
}
