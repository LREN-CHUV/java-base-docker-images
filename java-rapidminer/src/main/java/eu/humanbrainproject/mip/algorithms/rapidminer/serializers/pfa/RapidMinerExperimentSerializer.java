package eu.humanbrainproject.mip.algorithms.rapidminer.serializers.pfa;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;

import eu.humanbrainproject.mip.algorithms.rapidminer.InputData;
import eu.humanbrainproject.mip.algorithms.rapidminer.models.RapidMinerModel;
import eu.humanbrainproject.mip.algorithms.rapidminer.RapidMinerExperiment;
import eu.humanbrainproject.mip.algorithms.serializers.pfa.ExperimentSerializer;
import eu.humanbrainproject.mip.algorithms.serializers.pfa.InputDescription;


/**
 * @author Arnaud Jutzeler
 */
public class RapidMinerExperimentSerializer extends ExperimentSerializer<RapidMinerExperiment> {

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
    protected void writeError(RapidMinerExperiment value, JsonGenerator jgen) throws IOException {
        if (value.exception != null) {
            jgen.writeStringField("error", value.exception.getMessage());
        }
    }

    @Override
    protected void writeModel(RapidMinerExperiment value, JsonGenerator jgen) throws IOException {
        RapidMinerModel model = value.getModel();

        // Model representation
        if (model != null) {
            model.writeRepresentation(jgen);
        }
    }
}
