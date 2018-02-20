package eu.humanbrainproject.mip.algorithms.jsi.dummy;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;

import eu.humanbrainproject.mip.algorithms.jsi.serializers.pfa.ClusGenericSerializer;
import si.ijs.kt.clus.model.ClusModel;

public class DummyModelSerializer extends ClusGenericSerializer<ClusModel>{

    @Override
    public void writeModelConstants(ClusModel model, JsonGenerator jgen) throws IOException {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void writePfaAction(ClusModel model, JsonGenerator jgen) throws IOException {

        jgen.writeStartObject();
        {
            jgen.writeStringField("Hello", "World");
        }
        jgen.writeEndObject();

    }

}
