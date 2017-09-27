package eu.humanbrainproject.mip.algorithms.weka.serializers.pfa;

import com.fasterxml.jackson.core.JsonGenerator;
import eu.humanbrainproject.mip.algorithms.serializers.pfa.AlgorithmSerializer;
import eu.humanbrainproject.mip.algorithms.serializers.pfa.InputDescription;
import eu.humanbrainproject.mip.algorithms.weka.InputData;
import eu.humanbrainproject.mip.algorithms.weka.WekaAlgorithm;
import eu.humanbrainproject.mip.algorithms.weka.models.WekaClassifier;
import weka.classifiers.Classifier;

import java.io.IOException;


/**
 * @author Ludovic Claude
 */
public class WekaAlgorithmSerializer<M extends Classifier> extends AlgorithmSerializer<WekaAlgorithm<M>> {

    private final WekaModelSerializer<M> modelSerializer;

    public WekaAlgorithmSerializer(WekaModelSerializer<M> modelSerializer) {
        this.modelSerializer = modelSerializer;
    }

    @Override
    protected InputDescription getInputDescription(WekaAlgorithm<M> value) {
        InputData input = value.getInput();

        // Input, output
        if (input != null) {
            return new WekaInputDescription(input);
        } else {
            return null;
        }
    }

    @Override
    protected void writeModelConstants(WekaAlgorithm<M> value, JsonGenerator jgen) throws IOException {
        WekaClassifier<M> model = value.getModel();

        // Model representation
        if (model != null) {
            modelSerializer.writeModelConstants(model, jgen);
        }
    }

    @Override
    protected void writePfaBegin(WekaAlgorithm<M> value, JsonGenerator jgen) throws IOException {
        WekaClassifier<M> model = value.getModel();

        if (model != null) {
            modelSerializer.writePfaBegin(model, jgen);
        }
    }

    @Override
    protected void writePfaAction(WekaAlgorithm<M> value, JsonGenerator jgen) throws IOException {
        WekaClassifier<M> model = value.getModel();

        if (model != null) {
            modelSerializer.writePfaAction(model, jgen);
        }
    }

    @Override
    protected void writePfaEnd(WekaAlgorithm<M> value, JsonGenerator jgen) throws IOException {
        WekaClassifier<M> model = value.getModel();

        if (model != null) {
            modelSerializer.writePfaEnd(model, jgen);
        }
    }

    @Override
    protected void writePfaFunctionDefinitions(WekaAlgorithm<M> value, JsonGenerator jgen) throws IOException {
        WekaClassifier<M> model = value.getModel();

        if (model != null) {
            modelSerializer.writePfaFunctionDefinitions(model, jgen);
        }
    }

    @Override
    protected void writePfaPools(WekaAlgorithm<M> value, JsonGenerator jgen) throws IOException {
        WekaClassifier<M> model = value.getModel();

        if (model != null) {
            modelSerializer.writePfaPools(model, jgen);
        }
    }
}
