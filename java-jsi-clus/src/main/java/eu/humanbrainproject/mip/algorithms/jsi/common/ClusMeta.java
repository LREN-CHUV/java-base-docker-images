
package eu.humanbrainproject.mip.algorithms.jsi.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import eu.humanbrainproject.mip.algorithms.Algorithm.AlgorithmCapability;

/** @author Martin Breskvar */
public abstract class ClusMeta {

  public String NAME; // Name of the algorithm
  public String DOCUMENTATION; // Documentation for specific algorithm
  public Set<AlgorithmCapability> CAPABILITIES; // What are the algorithm capabilities
  public Map<String, Map<String, String>>
      SETTINGS; // Settings for the clus algorithm (this is algorithm-specific; should implement generateSettings())
  public List<String> CMDLINE_SWITCHES; // Command-line switches for clus
  public int
      WHICH_MODEL_TO_USE; // CLUS produces several models. Which one to use? DEFAULT=0, ORIGINAL=1, PRUNED=2, ...

  public ClusMeta() {
    NAME = "generic algorithm name";
    DOCUMENTATION = "generic algorithm documentation";
    CAPABILITIES = new HashSet<AlgorithmCapability>();
    SETTINGS = new HashMap<String, Map<String, String>>();
    CMDLINE_SWITCHES = new ArrayList<String>();
    WHICH_MODEL_TO_USE = -1;
  }
}
