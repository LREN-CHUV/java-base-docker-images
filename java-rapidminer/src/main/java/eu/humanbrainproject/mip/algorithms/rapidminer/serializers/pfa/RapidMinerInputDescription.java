package eu.humanbrainproject.mip.algorithms.rapidminer.serializers.pfa;

import com.rapidminer.example.Attribute;
import com.rapidminer.tools.Ontology;
import eu.humanbrainproject.mip.algorithms.db.DBException;
import eu.humanbrainproject.mip.algorithms.rapidminer.InputData;
import eu.humanbrainproject.mip.algorithms.rapidminer.RapidMinerAlgorithm;
import eu.humanbrainproject.mip.algorithms.rapidminer.exceptions.RapidMinerException;
import eu.humanbrainproject.mip.algorithms.serializers.pfa.InputDescription;

import java.util.LinkedList;
import java.util.List;

public class RapidMinerInputDescription extends InputDescription<RapidMinerAlgorithm<?>> {

    public RapidMinerInputDescription(RapidMinerAlgorithm<?> algorithm) {
        super(algorithm);
    }

    @Override
    protected VariableType getType(String variable) throws DBException, RapidMinerException {
        int valueType = getInputAttribute(variable).getValueType();
        switch (valueType) {
            case Ontology.REAL:
            case Ontology.NUMERICAL:
            case Ontology.INTEGER:
              return VariableType.REAL;
            default: return VariableType.CATEGORICAL_STRING;
        }
    }

    @Override
    protected List<String> getCategoricalValues(String variable) throws DBException, RapidMinerException {
        try {
            return getInputAttribute(variable).getMapping().getValues();
        } catch (UnsupportedOperationException e) {
            return new LinkedList<>();
        }
    }

    @Override
    protected String getQuery() {
        return getInput().getQuery();
    }

    @Override
    protected int getDataSize() throws DBException, RapidMinerException {
        return getInput().getData().size();
    }

    @Override
    protected String[] getVariables() {
        return new String[] {getInput().getVariableName()};
    }

    @Override
    protected String[] getCovariables() {
        return getInput().getFeaturesNames();
    }

    private InputData getInput() {
        return getAlgorithm().getInput();
    }

    private Attribute getInputAttribute(String variable) throws DBException, RapidMinerException {
        return getInput().getData().getAttributes().get(variable);
    }

}
