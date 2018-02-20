
package eu.humanbrainproject.mip.algorithms.jsi.dummy;

import java.util.Arrays;
import java.util.HashSet;
import eu.humanbrainproject.mip.algorithms.Algorithm.AlgorithmCapability;
import eu.humanbrainproject.mip.algorithms.jsi.common.ClusMeta;


public class DummyMeta extends ClusMeta {

    public DummyMeta() {
        super();

        this.NAME = "DummyAlgorithm";
        this.DOCUMENTATION = "This is the documentation for the dummy algorithm.";
        this.CAPABILITIES = new HashSet<AlgorithmCapability>(Arrays.asList(AlgorithmCapability.PREDICTIVE_MODEL));
        this.WHICH_MODEL_TO_USE = 0; // 0 = pruned, 1 = original, 2 = default
    }
}
