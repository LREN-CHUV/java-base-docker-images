package eu.humanbrainproject.mip.algorithms.serializers.pfa;

import eu.humanbrainproject.mip.algorithms.SimpleAlgorithm;

import java.util.Arrays;
import java.util.List;

public class MixedInputDescription extends InputDescription<SimpleAlgorithm> {

    public MixedInputDescription(SimpleAlgorithm algorithm) {
        super(algorithm);
    }

    @Override
    protected VariableType getType(String variable) throws Exception {
        if (variable.startsWith("num")) {
            return VariableType.REAL;
        } else if (variable.equals("cat2")) {
            return VariableType.CATEGORICAL_INT;
        } else {
            return VariableType.CATEGORICAL_STRING;
        }
    }

    @Override
    protected List<String> getCategoricalValues(String variable) throws Exception {
        switch (variable) {
            case "cat1": return Arrays.asList("a", "b", "c");
            case "cat2": return Arrays.asList("0", "1");
            default: throw new IllegalArgumentException("Not categorical: " + variable);
        }
    }

    @Override
    protected String getQuery() {
        return "SELECT input data";
    }

    @Override
    protected int getDataSize() throws Exception {
        return 10;
    }

    @Override
    protected String[] getVariables() {
        return new String[] {"var1"};
    }

    @Override
    protected String[] getCovariables() {
        return new String[] {"num1", "num2", "cat1", "cat2"};
    }

}
