package eu.humanbrainproject.mip.algorithms.weka.simplelr;

import com.fasterxml.jackson.core.JsonGenerator;
import eu.humanbrainproject.mip.algorithms.weka.WekaClassifier;
import eu.humanbrainproject.mip.algorithms.weka.serializers.pfa.WekaClassifierSerializer;
import weka.classifiers.functions.SimpleLinearRegression;

import java.io.IOException;

public class SimpleLinearRegressionSerializer extends WekaClassifierSerializer<SimpleLinearRegression> {

    @Override
    public void writeModelConstants(WekaClassifier<SimpleLinearRegression> model, JsonGenerator jgen) throws IOException {
        final SimpleLinearRegression lr = model.getTrainedClassifier();
        lr.getSlope()

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

    @Override
    public void writePfaAction(WekaClassifier<SimpleLinearRegression> model, JsonGenerator jgen) throws IOException {
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
