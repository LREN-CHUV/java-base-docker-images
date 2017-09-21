package eu.humanbrainproject.mip.algorithms.rapidminer.tests.models;

import java.util.Map;

import eu.humanbrainproject.mip.algorithms.rapidminer.models.RapidMinerModel;
import org.apache.commons.collections15.map.LinkedMap;

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

    public RPMDefault() {
        this(System.getProperty("PARAM_MODEL_method", System.getenv("PARAM_MODEL_method")));
    }

    public RPMDefault(String method) {
        super(DefaultLearner.class);
        this.method = method;
    }

    @Override
    public Map<String, String> getParameters() {
        LinkedMap<String, String> map = new LinkedMap<>();
        map.put("method", this.method);
        return map;
    }

}