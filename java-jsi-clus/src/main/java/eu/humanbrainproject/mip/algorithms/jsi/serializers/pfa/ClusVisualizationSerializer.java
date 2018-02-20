package eu.humanbrainproject.mip.algorithms.jsi.serializers.pfa;

import si.ijs.kt.clus.model.ClusModel;

public abstract class ClusVisualizationSerializer<M extends ClusModel> {
	public abstract String getVisualizationString(M model);
}
