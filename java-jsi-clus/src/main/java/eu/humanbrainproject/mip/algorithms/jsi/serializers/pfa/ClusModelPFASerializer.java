
package eu.humanbrainproject.mip.algorithms.jsi.serializers.pfa;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;

import eu.humanbrainproject.mip.algorithms.jsi.common.ClusAlgorithm;
import eu.humanbrainproject.mip.algorithms.jsi.common.ClusInputDescription;
import eu.humanbrainproject.mip.algorithms.serializers.pfa.AlgorithmSerializer;
import eu.humanbrainproject.mip.algorithms.serializers.pfa.InputDescription;
import si.ijs.kt.clus.model.ClusModel;

/** @author Martin Breskvar */
public class ClusModelPFASerializer<M extends ClusModel>
    extends AlgorithmSerializer<ClusAlgorithm<M>> {

  private final ClusGenericSerializer<M> modelSerializer;

  public ClusModelPFASerializer(ClusGenericSerializer<M> modelSerializer) {
    this.modelSerializer = modelSerializer;
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  protected InputDescription getInputDescription(ClusAlgorithm<M> value) {
    if (value.getInput() != null) {
      return new ClusInputDescription(value);
    } else {
      return null;
    }
  }

  @Override
  protected void writeModelConstants(ClusAlgorithm<M> value, JsonGenerator jgen)
      throws IOException {
    M model = value.getModel();

    // Model representation
    if (model != null) {
      modelSerializer.writeModelConstants(model, jgen);
    }
  }

  @Override
  protected void writePfaBegin(ClusAlgorithm<M> value, JsonGenerator jgen) throws IOException {
    M model = value.getModel();

    if (model != null) {
      modelSerializer.writePfaBegin(model, jgen);
    }
  }

  @Override
  protected void writePfaAction(ClusAlgorithm<M> value, JsonGenerator jgen) throws IOException {
    M model = value.getModel();

    if (model != null) {
      modelSerializer.writePfaAction(model, jgen);
    }
  }

  @Override
  protected void writePfaEnd(ClusAlgorithm<M> value, JsonGenerator jgen) throws IOException {
    M model = value.getModel();

    if (model != null) {
      modelSerializer.writePfaEnd(model, jgen);
    }
  }

  @Override
  protected void writePfaFunctionDefinitions(ClusAlgorithm<M> value, JsonGenerator jgen)
      throws IOException {
    M model = value.getModel();

    if (model != null) {
      modelSerializer.writePfaFunctionDefinitions(model, jgen);
    }
  }

  @Override
  protected void writePfaPools(ClusAlgorithm<M> value, JsonGenerator jgen) throws IOException {
    M model = value.getModel();

    if (model != null) {
      modelSerializer.writePfaPools(model, jgen);
    }
  }
}
