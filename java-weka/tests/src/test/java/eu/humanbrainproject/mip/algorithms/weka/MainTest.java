package eu.humanbrainproject.mip.algorithms.weka;

import com.opendatagroup.hadrian.reader.jsonToAst;
import eu.humanbrainproject.mip.algorithms.db.OutputDataConnector;
import eu.humanbrainproject.mip.algorithms.weka.serializers.pfa.WekaAlgorithmSerializer;
import eu.humanbrainproject.mip.algorithms.weka.serializers.pfa.WekaClassifierSerializer;
import eu.humanbrainproject.mip.algorithms.weka.simplelr.SimpleLinearRegressionSerializer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import weka.classifiers.functions.SimpleLinearRegression;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * Quasi-end-to-end tests
 *
 * @author Arnaud Jutzeler
 */
@DisplayName("With a Weka algorithms packaged in a Docker container")
public class MainTest {

    @Test
    @DisplayName("We can perform SimpleLinearRegression on one variable, one covariable")
    public void testOneVarOneCovar() throws Exception {

        String jobId = "001";

        System.setProperty("JOB_ID", jobId);
        System.setProperty("PARAM_query", "select iq, response_time_task2 from sample_data");
        System.setProperty("PARAM_variables", "iq");
        System.setProperty("PARAM_covariables", "response_time_task2");
        System.setProperty("PARAM_grouping", "");

        WekaClassifier<SimpleLinearRegression> classifier = new WekaClassifier<>(SimpleLinearRegression.class.getName());
        WekaClassifierSerializer<SimpleLinearRegression> classifierSerializer = new SimpleLinearRegressionSerializer();
        WekaAlgorithmSerializer<SimpleLinearRegression> algorithmSerializer = new WekaAlgorithmSerializer<>(classifierSerializer);

        Main<SimpleLinearRegression> main = new Main<>(classifier, algorithmSerializer);

        main.run();

        OutputDataConnector out = OutputDataConnector.fromEnv();
        OutputDataConnector.JobResults results = out.getJobResults(jobId);

        assertTrue(results != null);
        assertEquals("job_test", results.getNode());
        assertEquals("pfa_json", results.getResultsFormat().getShape());
        final String pfa = results.getResults();

        System.out.println(pfa);
        assertTrue(!pfa.contains("error"));
        assertTrue(pfa.contains("\"model\""));
        assertTrue(pfa.contains("\"action\""));

        // Validate PFA
        jsonToAst.apply(pfa);
    }

    @Test
    @DisplayName("We can perform SimpleLinearRegression on one variable, two covariables")
    public void testOneVarTwoCovars() throws Exception {

        String jobId = "002";

        System.setProperty("JOB_ID", jobId);
        System.setProperty("PARAM_query", "select iq, response_time_task2, college_math from sample_data");
        System.setProperty("PARAM_variables", "iq");
        System.setProperty("PARAM_covariables", "response_time_task2,college_math");
        System.setProperty("PARAM_grouping", "");

        WekaClassifier<SimpleLinearRegression> classifier = new WekaClassifier<>(SimpleLinearRegression.class.getName());
        WekaClassifierSerializer<SimpleLinearRegression> classifierSerializer = new SimpleLinearRegressionSerializer();
        WekaAlgorithmSerializer<SimpleLinearRegression> algorithmSerializer = new WekaAlgorithmSerializer<>(classifierSerializer);

        Main<SimpleLinearRegression> main = new Main<>(classifier, algorithmSerializer);

        main.run();

        OutputDataConnector out = OutputDataConnector.fromEnv();
        OutputDataConnector.JobResults results = out.getJobResults(jobId);

        assertTrue(results != null);
        assertEquals("job_test", results.getNode());
        assertEquals("pfa_json", results.getResultsFormat().getShape());
        final String pfa = results.getResults();

        System.out.println(pfa);
        assertTrue(!pfa.contains("error"));
        assertTrue(pfa.contains("\"model\""));
        assertTrue(pfa.contains("\"action\""));
    }

}
