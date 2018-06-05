package eu.humanbrainproject.mip.algorithms;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.postgresql.util.PGobject;

import java.sql.SQLException;
import java.util.Map;

public class Parameters {
    private String query;
    private String variables;
    private String[] covariables;
    private Map<String, String> modelParameters;

    public Parameters(String query, String variables, String[] covariables, Map<String, String> modelParameters) {
        this.query = query;
        this.variables = variables;
        this.covariables = covariables;
        this.modelParameters = modelParameters;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getVariables() {
        return variables;
    }

    public void setVariables(String variables) {
        this.variables = variables;
    }

    public String[] getCovariables() {
        return covariables;
    }

    public void setCovariables(String[] covariables) {
        this.covariables = covariables;
    }

    public Map<String, String> getModelParameters() {
        return modelParameters;
    }

    public void setModelParameters(Map<String, String> modelParameters) {
        this.modelParameters = modelParameters;
    }

    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public PGobject toPGObject() {
        PGobject jsonObject = new PGobject();
        jsonObject.setType("json");
        try {
            jsonObject.setValue(this.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
