package eu.humanbrainproject.mip.algorithms.weka.serializers.pfa;

import eu.humanbrainproject.mip.algorithms.db.DBException;
import eu.humanbrainproject.mip.algorithms.serializers.pfa.InputDescription;
import eu.humanbrainproject.mip.algorithms.weka.InputData;
import eu.humanbrainproject.mip.algorithms.weka.WekaAlgorithm;
import weka.core.Attribute;

import java.util.*;

public class WekaInputDescription<T extends WekaAlgorithm<?>> extends InputDescription<T> {

    public WekaInputDescription(T algorithm) {
        super(algorithm);
    }

    @Override
    protected VariableType getType(String variable) throws DBException {
        int valueType = getData().getData().attribute(variable).type();
        switch (valueType) {
            case Attribute.NUMERIC: return VariableType.REAL;
            default: return VariableType.CATEGORICAL_STRING;
        }
    }

    @Override
    protected List<String> getCategoricalValues(String variable) throws DBException {
        List<String> categories = new ArrayList<>();
        try {
            final Enumeration<Object> values = getData().getData().attribute(variable).enumerateValues();
            for (Object cat: Collections.list(values)) {
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
        return new String[] {getData().getVariableName()};
    }

    @Override
    protected String[] getCovariables() {
        return getData().getFeaturesNames();
    }

    private InputData getData() {
        return getAlgorithm().getInput();
    }
}
