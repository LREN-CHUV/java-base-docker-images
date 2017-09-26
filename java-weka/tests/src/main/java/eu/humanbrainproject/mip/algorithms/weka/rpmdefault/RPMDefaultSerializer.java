package eu.humanbrainproject.mip.algorithms.weka.rpmdefault;

import com.fasterxml.jackson.core.JsonGenerator;
import com.weka.operator.learner.lazy.DefaultModel;
import eu.humanbrainproject.mip.algorithms.weka.models.RapidMinerModel;
import eu.humanbrainproject.mip.algorithms.weka.serializers.pfa.RapidMinerModelSerializer;

import java.io.IOException;

public class RPMDefaultSerializer extends RapidMinerModelSerializer<DefaultModel> {

    @Override
    public void writeModelConstants(RapidMinerModel<DefaultModel> model, JsonGenerator jgen) throws IOException {
        String method = model.getParameters().get("method");
        if (!method.equals("attribute")) {
            jgen.writeObjectFieldStart("model");
            {
                jgen.writeObjectFieldStart("type");
                {
                    jgen.writeStringField("doc", "The model parameter");
                    jgen.writeStringField("name", "value");
                    jgen.writeStringField("type", (method.equals("mode")) ? "string" : "double");
                }
                jgen.writeEndObject();

                switch (method) {
                    case "median":
                    case "average":
                    case "constant":
                        jgen.writeNumberField("init", model.getTrainedModel().getValue());
                        break;
                    case "mode":
                        jgen.writeStringField("init", String.valueOf(model.getTrainedModel().getValue()));
                        break;
                    default:
                        throw new IllegalArgumentException("Cannot handle method: " + method);
                }
            }
            jgen.writeEndObject();
        }
    }

    @Override
    public void writePfaAction(RapidMinerModel<DefaultModel> model, JsonGenerator jgen) throws IOException {
        String method = model.getParameters().get("method");
        if (!method.equals("attribute")) {
            jgen.writeStartObject();
            {
                jgen.writeStringField("cell", "model");
            }
            jgen.writeEndObject();
        } else {
            String attributeName = model.getParameters().get("attribute_name");

            jgen.writeStartObject();
            {
                jgen.writeStringField("attr", "input");
                jgen.writeArrayFieldStart("path");
                {
                    jgen.writeString(attributeName);
                }
                jgen.writeEndArray();
            }
            jgen.writeEndObject();
        }
    }
}
