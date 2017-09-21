package eu.humanbrainproject.mip.algorithms.rapidminer.tests;

import eu.humanbrainproject.mip.algorithms.db.OutputDataConnector;
import eu.humanbrainproject.mip.algorithms.rapidminer.Main;
import eu.humanbrainproject.mip.algorithms.db.DBException;
import eu.humanbrainproject.mip.algorithms.rapidminer.serializers.pfa.RapidMinerAlgorithmSerializer;
import eu.humanbrainproject.mip.algorithms.rapidminer.tests.models.RPMDefault;
import eu.humanbrainproject.mip.algorithms.rapidminer.tests.models.RPMDefaultSerializer;
import org.junit.Test;

import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;


/**
 * Quasi-end-to-end tests
 *
 * @author Arnaud Jutzeler
 */
public class MainTest {

    @Test
    public void testOneVarOneCovar() throws DBException {

        System.out.println("We can perform RPMDefault on one variable, one covariable");

        String jobId = "001";

        System.setProperty("JOB_ID", jobId);
        System.setProperty("PARAM_query", "select iq, response_time_task2 from sample_data");
        System.setProperty("PARAM_variables", "iq");
        System.setProperty("PARAM_covariables", "response_time_task2");
        System.setProperty("PARAM_grouping", "");

        System.setProperty("PARAM_MODEL_method", "median");

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
        System.out.println(results.getResults());
        assertTrue(!results.getResults().contains("error"));
        assertTrue(results.getResults().contains("\"model\""));
        assertTrue(results.getResults().contains("\"action\""));

        // TODO Validate PFA
    }

    @Test
    public void testOneVarTwoCovars() throws DBException {

        System.out.println("We can perform RPMDefault on one variable, two covariables");

        String jobId = "002";

        System.setProperty("JOB_ID", jobId);
        System.setProperty("PARAM_query", "select iq, response_time_task2, college_math from sample_data");
        System.setProperty("PARAM_variables", "iq");
        System.setProperty("PARAM_covariables", "response_time_task2,college_math");
        System.setProperty("PARAM_grouping", "");

        System.setProperty("PARAM_MODEL_method", "median");

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
        System.out.println(results.getResults());
        assertTrue(!results.getResults().contains("error"));
        assertTrue(results.getResults().contains("\"model\""));
        assertTrue(results.getResults().contains("\"action\""));
    }

    @Test
    public void testInvalidModel() throws DBException {

        System.out.println("We cannot perform classification with invalid model");

        String jobId = "004";

        System.setProperty("JOB_ID", jobId);
        System.setProperty("PARAM_query", "select iq, response_time_task2, college_math from sample_data");
        System.setProperty("PARAM_variables", "iq");
        System.setProperty("PARAM_covariables", "response_time_task2,college_math");
        System.setProperty("PARAM_grouping", "");

        System.setProperty("PARAM_MODEL_method", "median");

        Main main = new Main(
                "eu.hbp.mip.container.rapidminer.tests.models.sjkhdfj",
                "eu.hbp.mip.container.rapidminer.tests.models.sjkhdfj.serialize",
                RapidMinerAlgorithmSerializer.class.getName()
        );

        main.run();

        OutputDataConnector out = OutputDataConnector.fromEnv();
        try {
            out.getJobResults(jobId);
            fail("Should not be able to fetch results");
        } catch (RuntimeException e) {
            assertEquals(SQLException.class, e.getCause().getClass());
        }
    }
}
