package eu.humanbrainproject.mip.algorithms.weka.serializers.pfa;

import eu.humanbrainproject.mip.algorithms.db.DBException;
import eu.humanbrainproject.mip.algorithms.serializers.pfa.InputDescription;
import eu.humanbrainproject.mip.algorithms.weka.InputData;
import weka.core.Attribute;

import java.util.*;

public class WekaInputDescription extends InputDescription {

    private final InputData data;

    public WekaInputDescription(InputData data) {
        this.data = data;
    }

    @Override
    protected VariableType getType(String variable) throws DBException {
        int valueType = data.getData().attribute(variable).type();
        switch (valueType) {
            case Attribute.NUMERIC: return VariableType.REAL;
            default: return VariableType.CATEGORICAL;
        }
    }

    @Override
    protected List<String> getCategoricalValues(String variable) throws DBException {
        List<String> categories = new ArrayList<>();
        try {
            final Enumeration<Object> values = data.getData().attribute(variable).enumerateValues();
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
        return data.getQuery();
    }

    @Override
    protected int getDataSize() throws DBException {
        return data.getData().size();
    }

    @Override
    protected String[] getVariables() {
        return new String[] {data.getVariableName()};
    }

    @Override
    protected String[] getCovariables() {
        return data.getFeaturesNames();
    }
}
