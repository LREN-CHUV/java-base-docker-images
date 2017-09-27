package eu.humanbrainproject.mip.algorithms.weka.serializers.pfa;

import com.fasterxml.jackson.core.JsonGenerator;
import eu.humanbrainproject.mip.algorithms.weka.WekaClassifier;
import weka.classifiers.Classifier;
import weka.classifiers.functions.SimpleLinearRegression;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class WekaClassifierSerializer<M extends Classifier> {

    private static final Logger LOGGER = Logger.getLogger(WekaClassifierSerializer.class.getName());


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

    @SuppressWarnings("unchecked")
    protected <T> T accessPrivateField(M model, String fieldName) {
        //TODO Remove this dirty and dangerous trick

        Class<? extends Classifier> modelClass = model.getClass();
        try {
            Field field = modelClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            return (T) field.get(model);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            String msg = "Cannot access private field " + fieldName + " from model " + modelClass;
            LOGGER.log(Level.SEVERE, msg, e);
            throw new RuntimeException(msg, e);
        }
    }


}
