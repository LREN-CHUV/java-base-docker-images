package eu.humanbrainproject.mip.algorithms.weka.serializers.pfa;

import com.weka.tools.Ontology;
import eu.humanbrainproject.mip.algorithms.db.DBException;
import eu.humanbrainproject.mip.algorithms.weka.InputData;
import eu.humanbrainproject.mip.algorithms.weka.exceptions.RapidMinerException;
import eu.humanbrainproject.mip.algorithms.serializers.pfa.InputDescription;

import java.util.LinkedList;
import java.util.List;

public class RapidMinerInputDescription extends InputDescription {

    private final InputData data;

    public RapidMinerInputDescription(InputData data) {
        this.data = data;
    }

    @Override
    protected VariableType getType(String variable) throws DBException, RapidMinerException {
        int valueType = data.getData().getAttributes().get(variable).getValueType();
        switch (valueType) {
            case Ontology.REAL: return VariableType.REAL;
            default: return VariableType.CATEGORICAL;
        }
    }

    @Override
    protected List<String> getCategoricalValues(String variable) throws DBException, RapidMinerException {
        try {
            return data.getData().getAttributes().get(variable).getMapping().getValues();
        } catch (UnsupportedOperationException e) {
            return new LinkedList<>();
        }
    }

    @Override
    protected String getQuery() {
        return data.getQuery();
    }

    @Override
    protected int getDataSize() throws DBException, RapidMinerException {
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
