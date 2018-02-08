
package eu.humanbrainproject.mip.algorithms.jsi.serializers.pfa;

import java.io.IOException;
import com.fasterxml.jackson.core.JsonGenerator;

import si.ijs.kt.clus.model.ClusModel;



public abstract class ClusGenericSerializer<M extends ClusModel> {

    public abstract void writeModelConstants(M model, JsonGenerator jgen) throws IOException;


    public void writePfaBegin(M model, JsonGenerator jgen) throws IOException {
        // Empty expression, to override if necessary
    }

    public void writePfaAction(M model, JsonGenerator jgen) throws IOException {
        // Empty expression, to override if necessary
    }

    public void writePfaEnd(M model, JsonGenerator jgen) throws IOException {
        // Empty expression, to override if necessary
    }

    public void writePfaFunctionDefinitions(M model, JsonGenerator jgen) throws IOException {
        // Empty set of functions, to override if necessary
    }

    public void writePfaPools(M model, JsonGenerator jgen) throws IOException {
        // Empty set of pools, to override if necessary
    }
}
