package eu.humanbrainproject.mip.algorithms.weka.serializers.pfa;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;

import com.weka.operator.learner.PredictionModel;
import eu.humanbrainproject.mip.algorithms.weka.InputData;
import eu.humanbrainproject.mip.algorithms.weka.WekaAlgorithm;
import eu.humanbrainproject.mip.algorithms.weka.models.WekaModel;
import eu.humanbrainproject.mip.algorithms.serializers.pfa.AlgorithmSerializer;
import eu.humanbrainproject.mip.algorithms.serializers.pfa.InputDescription;


/**
 * @author Arnaud Jutzeler
 */
public class RapidMinerAlgorithmSerializer<M extends PredictionModel> extends AlgorithmSerializer<WekaAlgorithm<M>> {

    private final RapidMinerModelSerializer<M> modelSerializer;

    public RapidMinerAlgorithmSerializer(RapidMinerModelSerializer<M> modelSerializer) {
        this.modelSerializer = modelSerializer;
    }

    @Override
    protected InputDescription getInputDescription(WekaAlgorithm<M> value) {
        InputData input = value.getInput();

        // Input, output
        if (input != null) {
            return new RapidMinerInputDescription(input);
        } else {
            return null;
        }
    }

    @Override
    protected void writeModelConstants(WekaAlgorithm<M> value, JsonGenerator jgen) throws IOException {
        WekaModel<M> model = value.getModel();

        // Model representation
        if (model != null) {
            modelSerializer.writeModelConstants(model, jgen);
        }
    }

    @Override
    protected void writePfaBegin(WekaAlgorithm<M> value, JsonGenerator jgen) throws IOException {
        WekaModel<M> model = value.getModel();

        if (model != null) {
            modelSerializer.writePfaBegin(model, jgen);
        }
    }

    @Override
    protected void writePfaAction(WekaAlgorithm<M> value, JsonGenerator jgen) throws IOException {
        WekaModel<M> model = value.getModel();

        if (model != null) {
            modelSerializer.writePfaAction(model, jgen);
        }
    }

    @Override
    protected void writePfaEnd(WekaAlgorithm<M> value, JsonGenerator jgen) throws IOException {
        WekaModel<M> model = value.getModel();

        if (model != null) {
            modelSerializer.writePfaEnd(model, jgen);
        }
    }

    @Override
    protected void writePfaFunctionDefinitions(WekaAlgorithm<M> value, JsonGenerator jgen) throws IOException {
        WekaModel<M> model = value.getModel();

        if (model != null) {
            modelSerializer.writePfaFunctionDefinitions(model, jgen);
        }
    }

    @Override
    protected void writePfaPools(WekaAlgorithm<M> value, JsonGenerator jgen) throws IOException {
        WekaModel<M> model = value.getModel();

        if (model != null) {
            modelSerializer.writePfaPools(model, jgen);
        }
    }
}
