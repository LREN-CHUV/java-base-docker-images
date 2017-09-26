package eu.humanbrainproject.mip.algorithms.weka.models;

import java.util.Map;

import eu.humanbrainproject.mip.algorithms.db.DBException;
import eu.humanbrainproject.mip.algorithms.weka.InputData;


/**
 *
 * Wrapper around RapidMiner Learner and corresponding Model
 * This is the only models dependent to be subclassed when integrating new RapidMiner algorithms
 *
 * @author Arnaud Jutzeler
 *
 */
public abstract class WekaModel<M extends PredictionModel> {

    private Class<? extends AbstractLearner> learnerClass;

    private Process process;

    private M trainedModel;

    public WekaModel(Class<? extends AbstractLearner> learnerClass) {
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
