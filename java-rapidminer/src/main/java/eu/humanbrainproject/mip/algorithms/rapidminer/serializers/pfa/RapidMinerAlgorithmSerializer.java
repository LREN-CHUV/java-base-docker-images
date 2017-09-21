package eu.humanbrainproject.mip.algorithms.rapidminer.serializers.pfa;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;

import eu.humanbrainproject.mip.algorithms.rapidminer.InputData;
import eu.humanbrainproject.mip.algorithms.rapidminer.models.RapidMinerModel;
import eu.humanbrainproject.mip.algorithms.rapidminer.RapidMinerExperiment;
import eu.humanbrainproject.mip.algorithms.serializers.pfa.AlgorithmSerializer;
import eu.humanbrainproject.mip.algorithms.serializers.pfa.InputDescription;


/**
 * @author Arnaud Jutzeler
 */
public class RapidMinerAlgorithmSerializer extends AlgorithmSerializer<RapidMinerExperiment> {

    private final RapidMinerModelSerializer modelSerializer;

    public RapidMinerAlgorithmSerializer(RapidMinerModelSerializer modelSerializer) {
        this.modelSerializer = modelSerializer;
    }

    @Override
    protected InputDescription getInputDescription(RapidMinerExperiment value) {
        InputData input = value.getInput();

        // Input, output
        if (input != null) {
            return new RapidMinerInputDescription(input);
        } else {
            return null;
        }
    }

    @Override
    protected void writeModelConstants(RapidMinerExperiment value, JsonGenerator jgen) throws IOException {
        RapidMinerModel model = value.getModel();

        // Model representation
        if (model != null) {
            modelSerializer.writeRepresentationModelConstants(model, jgen);
        }
    }

    @Override
    protected void writePfaBegin(RapidMinerExperiment value, JsonGenerator jgen) throws IOException {
        RapidMinerModel model = value.getModel();

        if (model != null) {
            modelSerializer.writePfaBegin(model, jgen);
        }
    }

    @Override
    protected void writePfaAction(RapidMinerExperiment value, JsonGenerator jgen) throws IOException {
        RapidMinerModel model = value.getModel();

        if (model != null) {
            modelSerializer.writePfaAction(model, jgen);
        }
    }

    @Override
    protected void writePfaEnd(RapidMinerExperiment value, JsonGenerator jgen) throws IOException {
        RapidMinerModel model = value.getModel();

        if (model != null) {
            modelSerializer.writePfaEnd(model, jgen);
        }
    }

    @Override
    protected void writePfaFunctionDefinitions(RapidMinerExperiment value, JsonGenerator jgen) throws IOException {
        RapidMinerModel model = value.getModel();

        if (model != null) {
            modelSerializer.writePfaFunctionDefinitions(model, jgen);
        }
    }

    @Override
    protected void writePfaPools(RapidMinerExperiment value, JsonGenerator jgen) throws IOException {
        RapidMinerModel model = value.getModel();

        if (model != null) {
            modelSerializer.writePfaPools(model, jgen);
        }
    }
}
