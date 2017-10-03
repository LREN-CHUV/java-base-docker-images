package eu.humanbrainproject.mip.algorithms.rapidminer.rpmdefault;

import java.util.*;

import eu.humanbrainproject.mip.algorithms.Algorithm.AlgorithmCapability;
import eu.humanbrainproject.mip.algorithms.Configuration;
import eu.humanbrainproject.mip.algorithms.rapidminer.models.RapidMinerModel;

import com.rapidminer.operator.learner.lazy.DefaultLearner;
import com.rapidminer.operator.learner.lazy.DefaultModel;


/**
 *
 * Trivial RapidMiner model 'DefautModel' for testing purpose only!
 *
 * @author Arnaud Jutzeler
 *
 */
public class RPMDefault extends RapidMinerModel<DefaultModel> {

    private static final Set<AlgorithmCapability> CAPABILITIES = new HashSet<>();

    private final HashMap<String, String> parameters;
    private final String method;

    static {
        CAPABILITIES.add(AlgorithmCapability.PREDICTIVE_MODEL);
    }

    @SuppressWarnings("unused")
    public RPMDefault() {
        this(Configuration.INSTANCE.algorithmParameterValues(Collections.singleton("method")).get("method"));
    }

    public RPMDefault(String method) {
        super(DefaultLearner.class);
        this.method = method;

        HashSet<String> parameterNames = new HashSet<>(Arrays.asList("method", "constant", "attribute_name"));
        parameters = new HashMap<>(Configuration.INSTANCE.algorithmParameterValues(parameterNames));
        parameters.put("method", this.method);

    }

    @Override
    public Map<String, String> getParameters() {
        return parameters;
    }

    @Override
    public Set<AlgorithmCapability> getCapabilities() {
        return CAPABILITIES;
    }

}