
package eu.humanbrainproject.mip.algorithms.jsi.serializers.pfa;

import si.ijs.kt.clus.algo.rules.ClusRuleSet;
import si.ijs.kt.clus.ext.featureRanking.Fimp;

/** @author Martin Breskvar */
public abstract class ClusDescriptiveSerializer {

  public abstract String getFimpString(Fimp fimp);

  public abstract String getRuleSetString(ClusRuleSet rules);
}
