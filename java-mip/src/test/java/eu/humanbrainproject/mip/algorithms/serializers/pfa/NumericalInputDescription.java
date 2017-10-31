package eu.humanbrainproject.mip.algorithms.serializers.pfa;

import eu.humanbrainproject.mip.algorithms.SimpleAlgorithm;

import java.util.Arrays;
import java.util.List;

public class NumericalInputDescription extends InputDescription<SimpleAlgorithm> {

    public NumericalInputDescription(SimpleAlgorithm algorithm) {
        super(algorithm);
    }

    @Override
    protected VariableType getType(String variable) throws Exception {
        return VariableType.REAL;
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
        return new String[] {"num1", "num2", "num3", "num4"};
    }

}
