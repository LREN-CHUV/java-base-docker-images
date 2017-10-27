package eu.humanbrainproject.mip.algorithms.rapidminer;

import com.opendatagroup.hadrian.reader.jsonToAst;
import eu.humanbrainproject.mip.algorithms.db.OutputDataConnector;
import eu.humanbrainproject.mip.algorithms.rapidminer.serializers.pfa.RapidMinerAlgorithmSerializer;
import eu.humanbrainproject.mip.algorithms.rapidminer.rpmdefault.RPMDefault;
import eu.humanbrainproject.mip.algorithms.rapidminer.rpmdefault.RPMDefaultSerializer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;


/**
 * Quasi-end-to-end tests
 *
 * @author Arnaud Jutzeler
 */
@DisplayName("With the RPMDefault RapidMiner algorithm, we can")
public class MainTest {

    @Test
    @DisplayName("perform RPMDefault on one variable, one covariable")
    public void testOneVarOneCovar() throws Exception {

        String jobId = "001";

        System.setProperty("JOB_ID", jobId);
        System.setProperty("PARAM_query", "select iq, response_time_task2 from sample_data");
        System.setProperty("PARAM_variables", "iq");
        System.setProperty("PARAM_covariables", "response_time_task2");
        System.setProperty("PARAM_grouping", "");

        System.setProperty("MODEL_PARAM_method", "median");

        Main main = new Main(
                RPMDefault.class.getName(),
                RPMDefaultSerializer.class.getName(),
                RapidMinerAlgorithmSerializer.class.getName()
        );

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
    @DisplayName("perform RPMDefault on one variable, two covariables")
    public void testOneVarTwoCovars() throws Exception {

        String jobId = "002";

        System.setProperty("JOB_ID", jobId);
        System.setProperty("PARAM_query", "select iq, response_time_task2, college_math from sample_data");
        System.setProperty("PARAM_variables", "iq");
        System.setProperty("PARAM_covariables", "response_time_task2,college_math");
        System.setProperty("PARAM_grouping", "");

        System.setProperty("MODEL_PARAM_method", "median");

        Main main = new Main(
                RPMDefault.class.getName(),
                RPMDefaultSerializer.class.getName(),
                RapidMinerAlgorithmSerializer.class.getName()
        );

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

    @Test
    @DisplayName("cannot perform classification with an invalid model")
    public void testInvalidModel() throws Exception {

        String jobId = "004";

        System.setProperty("JOB_ID", jobId);
        System.setProperty("PARAM_query", "select iq, response_time_task2, college_math from sample_data");
        System.setProperty("PARAM_variables", "iq");
        System.setProperty("PARAM_covariables", "response_time_task2,college_math");
        System.setProperty("PARAM_grouping", "");

        System.setProperty("MODEL_PARAM_method", "median");

        Main main = new Main(
                "eu.humanbrainproject.mip.algorithms.rapidminer.sjkhdfj",
                "eu.humanbrainproject.mip.algorithms.rapidminer.sjkhdfj.serialize",
                RapidMinerAlgorithmSerializer.class.getName()
        );

        try {
            main.run();
            fail("Should have thrown ClassNotFoundException");
        } catch (ClassNotFoundException expected) {
            // ok
        }

        OutputDataConnector out = OutputDataConnector.fromEnv();
        try {
            out.getJobResults(jobId);
            fail("Should not be able to fetch results");
        } catch (RuntimeException e) {
            assertEquals(SQLException.class, e.getCause().getClass());
        }
    }
}
