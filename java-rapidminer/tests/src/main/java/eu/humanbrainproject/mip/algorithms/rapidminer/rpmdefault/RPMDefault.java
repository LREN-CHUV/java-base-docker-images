package eu.humanbrainproject.mip.algorithms.rapidminer.rpmdefault;

import java.util.*;

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

    private String method;

    @SuppressWarnings("unused")
    public RPMDefault() {
        this(Configuration.INSTANCE.algorithmParameterValues(Collections.singleton("method")).get("method"));
    }

    public RPMDefault(String method) {
        super(DefaultLearner.class);
        this.method = method;
    }

    @Override
    public Map<String, String> getParameters() {
        HashSet<String> parameters = new HashSet<>(Arrays.asList("method", "constant", "attribute_name"));
        HashMap<String, String> map = new HashMap<>(Configuration.INSTANCE.algorithmParameterValues(parameters));
        map.put("method", this.method);
        return map;
    }

}