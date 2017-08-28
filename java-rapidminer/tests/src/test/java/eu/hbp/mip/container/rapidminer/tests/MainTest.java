package eu.hbp.mip.container.rapidminer.tests;

import eu.hbp.mip.container.rapidminer.Main;
import eu.hbp.mip.container.rapidminer.db.DBException;
import org.junit.Test;
import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import eu.hbp.mip.container.rapidminer.db.DBConnector;


/**
 * Quasi-end-to-end tests
 *
 * @author Arnaud Jutzeler
 *
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

		String[] args = {"eu.hbp.mip.container.rapidminer.tests.models.RPMDefault"};
		Main.main(args);

		DBConnector.DBResults results = DBConnector.getDBResult(jobId);

		assertTrue(results != null);
		assertEquals("job_test", results.node);
		assertEquals("pfa_json", results.shape);
		System.out.println(results.data);
		assertTrue(!results.data.contains("error"));

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

		String[] args = {"eu.hbp.mip.container.rapidminer.tests.models.RPMDefault"};
		Main.main(args);

		DBConnector.DBResults results = DBConnector.getDBResult(jobId);

		assertTrue(results != null);
		assertEquals("job_test", results.node);
		assertEquals("pfa_json", results.shape);
		System.out.println(results.data);
		assertTrue(!results.data.contains("error"));
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

		String[] args = {"eu.hbp.mip.container.rapidminer.tests.models.sjkhdfj"};
		Main.main(args);

		DBConnector.DBResults results = DBConnector.getDBResult(jobId);

		assertTrue(results == null);
	}
}
