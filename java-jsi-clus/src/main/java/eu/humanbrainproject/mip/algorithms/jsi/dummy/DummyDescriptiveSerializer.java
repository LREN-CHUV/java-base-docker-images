
package eu.humanbrainproject.mip.algorithms.jsi.dummy;

import eu.humanbrainproject.mip.algorithms.jsi.serializers.pfa.ClusDescriptiveSerializer;
import si.ijs.kt.clus.algo.rules.ClusRuleSet;
import si.ijs.kt.clus.ext.featureRanking.Fimp;

/** @author Martin Breskvar */
public class DummyDescriptiveSerializer extends ClusDescriptiveSerializer {

  @Override
  public String getFimpString(Fimp fimp) {
    return "DummyFimp";
  }

  @Override
  public String getRulesetString(ClusRuleSet rules) {
    return "DummyRules";
  }
}
