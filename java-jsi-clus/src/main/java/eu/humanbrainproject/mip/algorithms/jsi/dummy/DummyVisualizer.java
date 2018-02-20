
package eu.humanbrainproject.mip.algorithms.jsi.dummy;

import eu.humanbrainproject.mip.algorithms.jsi.serializers.pfa.ClusVisualizationSerializer;
import si.ijs.kt.clus.model.ClusModel;


/**
 * @author Martin Breskvar
 */

public class DummyVisualizer extends ClusVisualizationSerializer<ClusModel> {

    @Override
    public String getVisualizationString(ClusModel model) {
       return "DummyVisualization";
    }


}
