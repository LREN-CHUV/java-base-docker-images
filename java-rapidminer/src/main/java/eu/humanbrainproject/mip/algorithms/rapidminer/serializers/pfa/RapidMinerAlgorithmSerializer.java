package eu.humanbrainproject.mip.algorithms.rapidminer.serializers.pfa;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;

import com.rapidminer.operator.learner.PredictionModel;
import eu.humanbrainproject.mip.algorithms.rapidminer.InputData;
import eu.humanbrainproject.mip.algorithms.rapidminer.RapidMinerAlgorithm;
import eu.humanbrainproject.mip.algorithms.rapidminer.models.RapidMinerModel;
import eu.humanbrainproject.mip.algorithms.serializers.pfa.AlgorithmSerializer;
import eu.humanbrainproject.mip.algorithms.serializers.pfa.InputDescription;


/**
 * @author Arnaud Jutzeler
 */
public class RapidMinerAlgorithmSerializer<M extends PredictionModel> extends AlgorithmSerializer<RapidMinerAlgorithm<M>> {

    private final RapidMinerModelSerializer<M> modelSerializer;

    public RapidMinerAlgorithmSerializer(RapidMinerModelSerializer<M> modelSerializer) {
        this.modelSerializer = modelSerializer;
    }

    @Override
    protected InputDescription getInputDescription(RapidMinerAlgorithm<M> value) {
        InputData input = value.getInput();

        // Input, output
        if (input != null) {
            return new RapidMinerInputDescription(input);
        } else {
            return null;
        }
    }

    @Override
    protected void writeModelConstants(RapidMinerAlgorithm<M> value, JsonGenerator jgen) throws IOException {
        RapidMinerModel<M> model = value.getModel();

        // Model representation
        if (model != null) {
            modelSerializer.writeModelConstants(model, jgen);
        }
    }

    @Override
    protected void writePfaBegin(RapidMinerAlgorithm<M> value, JsonGenerator jgen) throws IOException {
        RapidMinerModel<M> model = value.getModel();

        if (model != null) {
            modelSerializer.writePfaBegin(model, jgen);
        }
    }

    @Override
    protected void writePfaAction(RapidMinerAlgorithm<M> value, JsonGenerator jgen) throws IOException {
        RapidMinerModel<M> model = value.getModel();

        if (model != null) {
            modelSerializer.writePfaAction(model, jgen);
        }
    }

    @Override
    protected void writePfaEnd(RapidMinerAlgorithm<M> value, JsonGenerator jgen) throws IOException {
        RapidMinerModel<M> model = value.getModel();

        if (model != null) {
            modelSerializer.writePfaEnd(model, jgen);
        }
    }

    @Override
    protected void writePfaFunctionDefinitions(RapidMinerAlgorithm<M> value, JsonGenerator jgen) throws IOException {
        RapidMinerModel<M> model = value.getModel();

        if (model != null) {
            modelSerializer.writePfaFunctionDefinitions(model, jgen);
        }
    }

    @Override
    protected void writePfaPools(RapidMinerAlgorithm<M> value, JsonGenerator jgen) throws IOException {
        RapidMinerModel<M> model = value.getModel();

        if (model != null) {
            modelSerializer.writePfaPools(model, jgen);
        }
    }
}
