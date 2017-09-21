package eu.humanbrainproject.mip.algorithms.rapidminer.serializers.pfa;

import com.fasterxml.jackson.core.JsonGenerator;
import com.rapidminer.operator.learner.PredictionModel;
import eu.humanbrainproject.mip.algorithms.rapidminer.models.RapidMinerModel;

import java.io.IOException;

public abstract class RapidMinerModelSerializer<M extends PredictionModel> {

    public abstract void writeRepresentationModelConstants(RapidMinerModel<M> model, JsonGenerator jgen) throws IOException;

    public void writePfaBegin(RapidMinerModel<M> model, JsonGenerator jgen) throws IOException {}

    public void writePfaAction(RapidMinerModel<M> model, JsonGenerator jgen) throws IOException {}

    public void writePfaEnd(RapidMinerModel<M> model, JsonGenerator jgen) throws IOException {}

    public void writePfaFunctionDefinitions(RapidMinerModel<M> model, JsonGenerator jgen) throws IOException {}

    public void writePfaPools(RapidMinerModel<M> model, JsonGenerator jgen) throws IOException {}
}
