package eu.humanbrainproject.mip.algorithms.rapidminer;

import java.io.IOException;
import java.util.logging.Logger;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import com.rapidminer.RapidMiner;
import com.rapidminer.operator.*;

import eu.humanbrainproject.mip.algorithms.Experiment;
import eu.humanbrainproject.mip.algorithms.db.DBException;
import eu.humanbrainproject.mip.algorithms.rapidminer.exceptions.RapidMinerException;
import eu.humanbrainproject.mip.algorithms.rapidminer.models.RapidMinerModel;
import eu.humanbrainproject.mip.algorithms.rapidminer.serializers.pfa.RapidMinerExperimentSerializer;


/**
 * Default experiment consisting of training and validating a specific models
 * The models is:
 * 1) Trained using all the data
 *
 * @author Arnaud Jutzeler
 */
public class RapidMinerExperiment implements Experiment {

    private static final Logger LOGGER = Logger.getLogger(RapidMinerExperiment.class.getName());

    public static final String NAME = "rapidminer";
    public static final String DOCUMENTATION = "RapidMiner Model";

    private static boolean isRPMInit = false;

    private InputData input;
    private RapidMinerModel model;

    public Exception exception;

    public RapidMinerExperiment(RapidMinerModel model) {
        this(null, model);
    }

    /**
     * @param input
     * @param model
     */
    public RapidMinerExperiment(InputData input, RapidMinerModel model) {
        this.input = input;
        this.model = model;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getDocumentation() {
        return DOCUMENTATION;
    }

    /**
     * @return
     */
    public InputData getInput() {
        return input;
    }

    /**
     * @return
     */
    public RapidMinerModel getModel() {
        return model;
    }

    /**
     * @throws RapidMinerException
     */
    public void run() throws RapidMinerException, DBException {

        try {
            if (!isRPMInit) {
                initializeRPM();
            }

            if (model.isAlreadyTrained() || exception != null) {
                LOGGER.warning("This experiment was already executed!");
                return;
            }

            if (input == null) {
                input = InputData.fromEnv();
            }

            // Train the model
            model.train(input);

        } catch (OperatorCreationException | OperatorException | ClassCastException ex) {
            final RapidMinerException rapidMinerException = new RapidMinerException(ex);
            this.exception = rapidMinerException;
            throw rapidMinerException;

        } catch (DBException | RapidMinerException ex) {
            this.exception = ex;
            throw ex;
        }
    }

    /**
     * Generate the RMP representation of the experiment
     * which is the native xml format used by RapidMiner
     *
     * @return the native xml representation of the RapidMiner process
     */
    public String toRMP() {
        return model.toRMP();
    }

    /**
     * Generate the PFA representation of the experiment outcome
     *
     * @return
     * @throws IOException
     */
    public String toPFA() throws IOException {
        ObjectMapper myObjectMapper = getObjectMapper();
        return myObjectMapper.writeValueAsString(this);
    }

    /**
     * Generate the PFA representation of the experiment outcome
     *
     * @return
     * @throws IOException
     */
    public String toPrettyPFA() throws IOException {
        ObjectMapper myObjectMapper = getObjectMapper();
        return myObjectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
    }

    private ObjectMapper getObjectMapper() {
        ObjectMapper myObjectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule("RapidMiner", new Version(1, 0, 0, null, null, null));
        module.addSerializer(RapidMinerExperiment.class, new RapidMinerExperimentSerializer());
        myObjectMapper.registerModule(module);
        return myObjectMapper;
    }

    /**
     * Initialize RapidMiner
     * Must be run only once
     */
    private static void initializeRPM() {
        // Init RapidMiner
        System.setProperty("rapidminer.home", System.getProperty("user.dir"));

        RapidMiner.setExecutionMode(RapidMiner.ExecutionMode.COMMAND_LINE);
        RapidMiner.init();
        isRPMInit = true;
    }

}
