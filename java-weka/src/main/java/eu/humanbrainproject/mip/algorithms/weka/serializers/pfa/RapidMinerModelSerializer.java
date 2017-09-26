package eu.humanbrainproject.mip.algorithms.weka.serializers.pfa;

import com.fasterxml.jackson.core.JsonGenerator;
import com.weka.operator.learner.PredictionModel;
import eu.humanbrainproject.mip.algorithms.weka.models.WekaModel;

import java.io.IOException;

public abstract class RapidMinerModelSerializer<M extends PredictionModel> {

    public abstract void writeModelConstants(WekaModel<M> model, JsonGenerator jgen) throws IOException;

    public void writePfaBegin(WekaModel<M> model, JsonGenerator jgen) throws IOException {
        // Empty expression, to override if necessary
    }

    public void writePfaAction(WekaModel<M> model, JsonGenerator jgen) throws IOException {
        // Empty expression, to override if necessary
    }

    public void writePfaEnd(WekaModel<M> model, JsonGenerator jgen) throws IOException {
        // Empty expression, to override if necessary
    }

    public void writePfaFunctionDefinitions(WekaModel<M> model, JsonGenerator jgen) throws IOException {
        // Empty set of functions, to override if necessary
    }

    public void writePfaPools(WekaModel<M> model, JsonGenerator jgen) throws IOException {
        // Empty set of pools, to override if necessary
    }
}
