
package eu.humanbrainproject.mip.algorithms.jsi.dummy;

import eu.humanbrainproject.mip.algorithms.jsi.serializers.pfa.ClusFimpSerializer;
import si.ijs.kt.clus.ext.featureRanking.Fimp;


/**
 * 
 * @author Martin Breskvar
 *
 */
public class DummyFimpSerializer extends ClusFimpSerializer {

    @Override
    public String getFimpString(Fimp fimp) {
        return "DummyFimp";
    }
}
