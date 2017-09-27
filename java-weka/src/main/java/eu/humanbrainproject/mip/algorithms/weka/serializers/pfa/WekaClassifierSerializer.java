package eu.humanbrainproject.mip.algorithms.weka.serializers.pfa;

import com.fasterxml.jackson.core.JsonGenerator;
import eu.humanbrainproject.mip.algorithms.weka.WekaClassifier;
import weka.classifiers.Classifier;

import java.io.IOException;

public abstract class WekaClassifierSerializer<M extends Classifier> {

    public abstract void writeModelConstants(WekaClassifier<M> model, JsonGenerator jgen) throws IOException;

    public void writePfaBegin(WekaClassifier<M> model, JsonGenerator jgen) throws IOException {
        // Empty expression, to override if necessary
    }

    public void writePfaAction(WekaClassifier<M> model, JsonGenerator jgen) throws IOException {
        // Empty expression, to override if necessary
    }

    public void writePfaEnd(WekaClassifier<M> model, JsonGenerator jgen) throws IOException {
        // Empty expression, to override if necessary
    }

    public void writePfaFunctionDefinitions(WekaClassifier<M> model, JsonGenerator jgen) throws IOException {
        // Empty set of functions, to override if necessary
    }

    public void writePfaPools(WekaClassifier<M> model, JsonGenerator jgen) throws IOException {
        // Empty set of pools, to override if necessary
    }
}
