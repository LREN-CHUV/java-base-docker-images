package eu.humanbrainproject.mip.algorithms.weka.simplelr;

import com.fasterxml.jackson.core.JsonGenerator;
import com.google.common.base.Charsets;
import com.google.common.collect.Maps;
import com.google.common.io.Resources;
import com.hubspot.jinjava.Jinjava;
import eu.humanbrainproject.mip.algorithms.weka.WekaClassifier;
import eu.humanbrainproject.mip.algorithms.weka.serializers.pfa.WekaClassifierSerializer;
import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;
import weka.classifiers.functions.SimpleLinearRegression;
import weka.core.Attribute;

import java.io.IOException;
import java.util.Map;

public class SimpleLinearRegressionSerializer extends WekaClassifierSerializer<SimpleLinearRegression> {

    @Override
    public void writeModelConstants(WekaClassifier<SimpleLinearRegression> model, JsonGenerator jgen) throws IOException {
        final SimpleLinearRegression lr = model.getTrainedClassifier();

        jgen.writeObjectFieldStart("model");
        {
            jgen.writeFieldName("type");
            {
                Schema schema = SchemaBuilder.record("LinearModel").doc("The model parameter").fields()
                            /* */.name("slope").type().doubleType().noDefault()
                            /* */.name("intercept").type().doubleType().noDefault()
                            /* */.name("classMeanForMissing").type().doubleType().noDefault()
                            /* */.name("selectedVariable").type().stringType().noDefault()
                        .endRecord();

                jgen.writeRawValue(schema.toString());
            }

            jgen.writeObjectFieldStart("init");
            {
                jgen.writeNumberField("intercept", lr.getIntercept());
                if (lr.foundUsefulAttribute()) {
                    Attribute attribute = accessPrivateField(lr, "m_attribute");
                    Double classMeanForMissing = accessPrivateField(lr, "m_classMeanForMissing");

                    jgen.writeNumberField("slope", lr.getSlope());
                    jgen.writeNumberField("classMeanForMissing", classMeanForMissing);
                    jgen.writeStringField("selectedVariable", attribute.name());
                } else {
                    jgen.writeStringField("selectedVariable", null);
                }
            }
            jgen.writeEndObject();
        }
        jgen.writeEndObject();
    }

    @Override
    public void writePfaAction(WekaClassifier<SimpleLinearRegression> model, JsonGenerator jgen) throws IOException {
        Jinjava jinjava = new Jinjava();
        Map<String, Object> context = Maps.newHashMap();
        final SimpleLinearRegression lr = model.getTrainedClassifier();
        context.put("foundUsefulAttribute", lr.foundUsefulAttribute());
        if (lr.foundUsefulAttribute()) {
            Attribute attribute = accessPrivateField(lr, "m_attribute");
            context.put("selectedVariable", attribute.name());
        } else {
            context.put("selectedVariable", null);
        }

        String template = Resources.toString(this.getClass().getResource("action.jinja"), Charsets.UTF_8);
        String renderedTemplate = jinjava.render(template, context);

        jgen.writeRaw(renderedTemplate);
    }

}
