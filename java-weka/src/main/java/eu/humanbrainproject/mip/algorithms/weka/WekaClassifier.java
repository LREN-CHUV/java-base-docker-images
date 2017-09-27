package eu.humanbrainproject.mip.algorithms.weka;

import eu.humanbrainproject.mip.algorithms.Configuration;
import weka.classifiers.AbstractClassifier;
import weka.classifiers.Classifier;
import weka.core.CommandlineRunnable;
import weka.core.Option;
import weka.core.OptionHandler;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * Wrapper around Weka Classifier
 * This is the only models dependent to be subclassed when integrating new Weka algorithms
 *
 * @author Ludovic Claude
 *
 */
public class WekaClassifier<M extends Classifier> {

    private static final Logger LOGGER = Logger.getLogger(WekaClassifier.class.getName());

    private final String classifierName;
    private final String[] options;

    private M trainedClassifier;

    public WekaClassifier(String classifierName) {
        this(classifierName, optionsFromEnv(classifierName));
    }

    public WekaClassifier(String classifierName, String[] options) {
        this.classifierName = classifierName;
        this.options = options;
    }

    public String getClassifierName() {
        return classifierName;
    }

    public String[] getOptions() {
        return options;
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
                    LOGGER.log(Level.SEVERE, "Cannot train Weka classifier", e);
                } else {
                    LOGGER.severe(e.getMessage());
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

    public boolean isAlreadyTrained() {
        return getTrainedClassifier() != null;
    }

    public M getTrainedClassifier() {
        return trainedClassifier;
    }

    public static List<Option> availableOptions(String classifierName) throws Exception {
        Classifier classifier = AbstractClassifier.forName(classifierName, new String[0]);
        if (classifier instanceof OptionHandler) {
            OptionHandler optionHandler = (OptionHandler) classifier;
            return Collections.list(optionHandler.listOptions());
        } else {
            return Collections.emptyList();
        }
    }

    private static String[] optionsFromEnv(String classifierName) {
        List<String> options = new ArrayList<>();
        try {
            Set<String> params = new HashSet<>();
            Map<String, Option> allOptions = new HashMap<>();
            for (Option option: availableOptions(classifierName)) {
                params.add(option.name());
                allOptions.put(option.name(), option);
            }
            HashMap<String, String> values = new HashMap<>(Configuration.INSTANCE.algorithmParameterValues(params));
            for (String param: values.keySet()) {
                Option option = allOptions.get(param);
                if (option.numArguments() == 0) {
                    options.add(option.synopsis());
                } else if (option.numArguments() == 1) {
                    options.add(option.synopsis());
                    options.add(values.get(param));
                } else {
                    options.add(option.synopsis());
                    final String optionValues = values.get(param);
                    StringTokenizer st = new StringTokenizer(optionValues, " ,");
                    while (st.hasMoreTokens()) {
                        options.add(st.nextToken());
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return options.toArray(new String[options.size()]);
    }
}
