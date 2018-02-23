
package eu.humanbrainproject.mip.algorithms.jsi.common;

import eu.humanbrainproject.mip.algorithms.db.DBException;
import eu.humanbrainproject.mip.algorithms.serializers.pfa.InputDescription;
import weka.core.Attribute;

import java.util.*;

/** @author Martin Breskvar */
@SuppressWarnings("rawtypes")
public class ClusInputDescription<T extends ClusAlgorithm> extends InputDescription<T> {

  public ClusInputDescription(T algorithm) {
    super(algorithm);
  }

  @Override
  protected VariableType getType(String variable) throws DBException {
    int valueType = getData().getData().attribute(variable).type();
    switch (valueType) {
      case Attribute.NUMERIC:
        return VariableType.REAL;
      default:
        return VariableType.CATEGORICAL_STRING;
    }
  }

  @Override
  protected List<String> getCategoricalValues(String variable) throws DBException {
    List<String> categories = new ArrayList<>();
    try {
      final Enumeration<Object> values = getData().getData().attribute(variable).enumerateValues();
      for (Object cat : Collections.list(values)) {
        if (cat != null) {
          categories.add(cat.toString());
        }
      }
    } catch (UnsupportedOperationException e) {
      return new LinkedList<>();
    }
    return categories;
  }

  @Override
  protected String getQuery() {
    return getData().getQuery();
  }

  @Override
  protected int getDataSize() throws DBException {
    return getData().getData().size();
  }

  @Override
  protected String[] getVariables() {
    return getData().getOutputFeaturesNames();
  }

  @Override
  protected String[] getCovariables() {
    return getData().getInputFeaturesNames();
  }

  private InputData getData() {
    return getAlgorithm().getInput();
  }
}
