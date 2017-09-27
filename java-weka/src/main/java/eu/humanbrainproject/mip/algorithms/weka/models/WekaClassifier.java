package eu.humanbrainproject.mip.algorithms.weka.models;

import eu.humanbrainproject.mip.algorithms.weka.InputData;
import weka.classifiers.AbstractClassifier;
import weka.classifiers.Classifier;
import weka.core.CommandlineRunnable;

import java.util.Map;


/**
 *
 * Wrapper around Weka Classifier
 * This is the only models dependent to be subclassed when integrating new Weka algorithms
 *
 * @author Ludovic Claude
 *
 */
public abstract class WekaClassifier<M extends Classifier> {

    private final String classifierName;
    private final String[] options;

    private M trainedClassifier;

    public WekaClassifier(String classifierName, String[] options) {
        this.classifierName = classifierName;
        this.options = options;
    }

    @SuppressWarnings("unchecked")
    public void train(InputData trainingData) throws Exception {

        trainedClassifier = (M) AbstractClassifier.forName(classifierName, options);

            try {
                if (trainedClassifier instanceof CommandlineRunnable) {
                    ((CommandlineRunnable)trainedClassifier).preExecution();
                }
                trainedClassifier.buildClassifier(trainingData.getData());
            } catch (Exception e) {
                if (((e.getMessage() != null)
                        && (!e.getMessage().contains("General options")))
                        || (e.getMessage() == null)) {
                    e.printStackTrace();
                } else {
                    System.err.println(e.getMessage());
                }
            }
            if (trainedClassifier instanceof CommandlineRunnable) {
                try {
                    ((CommandlineRunnable) trainedClassifier).postExecution();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

    }

    public abstract Map<String,String> getParameters();

    public boolean isAlreadyTrained() {
        return getTrainedClassifier() != null;
    }

    public M getTrainedClassifier() {
        return trainedClassifier;
    }


}
