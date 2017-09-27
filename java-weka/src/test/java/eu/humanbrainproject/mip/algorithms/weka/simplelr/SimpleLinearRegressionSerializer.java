package eu.humanbrainproject.mip.algorithms.weka.simplelr;

import com.fasterxml.jackson.core.JsonGenerator;
import eu.humanbrainproject.mip.algorithms.weka.WekaClassifier;
import eu.humanbrainproject.mip.algorithms.weka.serializers.pfa.WekaClassifierSerializer;
import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;
import weka.classifiers.functions.SimpleLinearRegression;
import weka.core.Attribute;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SimpleLinearRegressionSerializer extends WekaClassifierSerializer<SimpleLinearRegression> {

    @Override
    public void writeModelConstants(WekaClassifier<SimpleLinearRegression> model, JsonGenerator jgen) throws IOException {
        final SimpleLinearRegression lr = model.getTrainedClassifier();

        jgen.writeObjectFieldStart("model");
        {
            jgen.writeObjectFieldStart("type");
            {
                jgen.writeStringField("doc", "The model parameter");
                jgen.writeStringField("name", "value");
                jgen.writeFieldName("type");
                {
                    Schema schema = SchemaBuilder.record("LinearModel").fields()
                                /* */.name("slope").type().doubleType().noDefault()
                                /* */.name("intercept").type().doubleType().noDefault()
                                /* */.name("classMeanForMissing").type().doubleType().noDefault()
                                /* */.name("selectedVariable").type().stringType().stringDefault(null)
                            .endRecord();

                    jgen.writeRawValue(schema.toString());
                }
            }
            jgen.writeEndObject();

            jgen.writeObjectFieldStart("init");
            {
                jgen.writeNumberField("intercept", lr.getIntercept());
                if (lr.foundUsefulAttribute()) {
                    Attribute attribute = accessPrivateField(lr, "m_attribute");
                    Double classMeanForMissing = accessPrivateField(lr, "m_classMeanForMissing");

                    jgen.writeNumberField("slope", lr.getSlope());
                    jgen.writeNumberField("classMeanForMissing", classMeanForMissing);
                    jgen.writeStringField("selectedVariable", attribute.name());
                }
            }
        }
        jgen.writeEndObject();
    }

    @Override
    public void writePfaAction(WekaClassifier<SimpleLinearRegression> model, JsonGenerator jgen) throws IOException {
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
