package eu.humanbrainproject.mip.algorithms.rapidminer.models;

import java.util.Map;

import eu.humanbrainproject.mip.algorithms.db.DBException;
import eu.humanbrainproject.mip.algorithms.rapidminer.InputData;

import com.rapidminer.example.ExampleSet;
import com.rapidminer.operator.IOContainer;
import com.rapidminer.operator.Operator;
import com.rapidminer.operator.OperatorCreationException;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.learner.AbstractLearner;
import com.rapidminer.operator.learner.PredictionModel;
import com.rapidminer.tools.OperatorService;
import com.rapidminer.Process;
import eu.humanbrainproject.mip.algorithms.rapidminer.exceptions.RapidMinerException;


/**
 *
 * Wrapper around RapidMiner Learner and corresponding Model
 * This is the only models dependent to be subclassed when integrating new RapidMiner algorithms
 *
 * @author Arnaud Jutzeler
 *
 */
public abstract class RapidMinerModel<M extends PredictionModel> {

    private Class<? extends AbstractLearner> learnerClass;

    private Process process;

    private M trainedModel;

    public RapidMinerModel(Class<? extends AbstractLearner> learnerClass) {
        this.learnerClass = learnerClass;
    }

    @SuppressWarnings("unchecked")
    public void train(InputData trainingData) throws DBException, RapidMinerException {

        // Create the RapidMiner process
        this.process = new Process();

        try {

            // Model training
            Operator modelOp = OperatorService.createOperator(this.learnerClass);
            Map<String, String> parameters = getParameters();
            for (Map.Entry<String, String> entry : parameters.entrySet()) {
                modelOp.setParameter(entry.getKey(), entry.getValue());
            }
            getProcess().getRootOperator().getSubprocess(0).addOperator(modelOp);
            getProcess().getRootOperator()
                    .getSubprocess(0)
                    .getInnerSources()
                    .getPortByIndex(0)
                    .connectTo(modelOp.getInputPorts().getPortByName("training set"));
            modelOp.getOutputPorts().getPortByName("model").connectTo(getProcess().getRootOperator()
                    .getSubprocess(0)
                    .getInnerSinks()
                    .getPortByIndex(0));

            // Run process
            ExampleSet exampleSet = trainingData.getData();
            IOContainer ioResult = getProcess().run(new IOContainer(exampleSet, exampleSet, exampleSet));

            trainedModel = (M) ioResult.get(PredictionModel.class, 0);

        } catch (OperatorException | OperatorCreationException e) {
            throw new RapidMinerException(e);
        }
    }

    public abstract Map<String,String> getParameters();

    public String toRMP() {
        return getProcess().getRootOperator().getXML(false);
    }

    public boolean isAlreadyTrained() {
        return getTrainedModel() != null;
    }

    public Process getProcess() {
        return process;
    }

    public M getTrainedModel() {
        return trainedModel;
    }


}
