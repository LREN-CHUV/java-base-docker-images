package eu.humanbrainproject.mip.algorithms.rapidminer;

import java.io.IOException;

import eu.humanbrainproject.mip.algorithms.rapidminer.db.DBConnector;
import eu.humanbrainproject.mip.algorithms.rapidminer.db.DBException;
import eu.humanbrainproject.mip.algorithms.rapidminer.models.RapidMinerModel;


/**
 *
 * Entrypoint
 *
 * @author Arnaud Jutzeler
 *
 */
public class Main {

	public static void main(String[] args) {

		try {
			Class modelClass = Class.forName(args[0]);
			RapidMinerModel model = (RapidMinerModel) modelClass.newInstance();

			// Run experiment
			RapidMinerExperiment experiment = new RapidMinerExperiment(model);
			experiment.run();

			// Write results PFA in DB
			String pfa = experiment.toPFA();
			DBConnector.saveResults(experiment);

		} catch (ClassNotFoundException e) {
			System.err.println(e.getMessage());
		} catch (InstantiationException | IllegalAccessException |
				IOException | DBException | ClassCastException e) {
			e.printStackTrace();
		}
	}
}
