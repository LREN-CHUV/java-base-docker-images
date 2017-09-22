package eu.humanbrainproject.mip.algorithms.rapidminer.rpmdefault;

import com.fasterxml.jackson.core.JsonGenerator;
import com.rapidminer.operator.learner.lazy.DefaultModel;
import eu.humanbrainproject.mip.algorithms.rapidminer.models.RapidMinerModel;
import eu.humanbrainproject.mip.algorithms.rapidminer.serializers.pfa.RapidMinerModelSerializer;

import java.io.IOException;

public class RPMDefaultSerializer extends RapidMinerModelSerializer<DefaultModel> {

    @Override
    public void writeRepresentationModelConstants(RapidMinerModel<DefaultModel> model, JsonGenerator jgen) throws IOException {
        jgen.writeObjectFieldStart("model");
        {
            jgen.writeObjectFieldStart("type");
            {
                jgen.writeStringField("doc", "The model parameter");
                jgen.writeStringField("name", "value");
                jgen.writeStringField("type", "double");
            }
            jgen.writeEndObject();

            jgen.writeObjectFieldStart("init");
            {
                jgen.writeNumberField("value", model.getTrainedModel().getValue());
            }
            jgen.writeEndObject();
        }
        jgen.writeEndObject();
    }

    @Override
    public void writePfaAction(RapidMinerModel<DefaultModel> model, JsonGenerator jgen) throws IOException {
        jgen.writeStringField("cell", "model");
    }
}
