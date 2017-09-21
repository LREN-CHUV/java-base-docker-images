package eu.humanbrainproject.mip.algorithms.rapidminer.tests;

import eu.humanbrainproject.mip.algorithms.rapidminer.RapidMinerExperiment;
import eu.humanbrainproject.mip.algorithms.rapidminer.models.RapidMinerModel;

import eu.humanbrainproject.mip.algorithms.rapidminer.serializers.pfa.RapidMinerAlgorithmSerializer;
import eu.humanbrainproject.mip.algorithms.rapidminer.tests.models.RPMDefault;

import eu.humanbrainproject.mip.algorithms.rapidminer.tests.models.RPMDefaultSerializer;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Tests for RapidMinerExperiment
 *
 * @author Arnaud Jutzeler
 */
public class RapidMinerExperimentTest {

    @Test
    public void test_classification() throws Exception {

        String[] featureNames = new String[]{"input1", "input2"};
        String variableName = "output";
        double[][] data = new double[][]{
                {1.2, 2.4},
                {6.7, 8.9},
                {4.6, 23.4},
                {7.6, 5.4},
                {1.2, 1.6},
                {3.4, 4.7},
                {3.4, 6.5}};
        String[] labels = new String[]{"YES", "NO", "YES", "NO", "YES", "NO", "NO"};

        // Get experiment input
        ClassificationInputData input = new ClassificationInputData(featureNames, variableName, data, labels);
        RapidMinerModel model = new RPMDefault("mode");

        // Run experiment
        final RapidMinerAlgorithmSerializer serializer = new RapidMinerAlgorithmSerializer(new RPMDefaultSerializer());
        final RapidMinerExperiment experiment = new RapidMinerExperiment(input, model, serializer);
        experiment.run();

        System.out.println(experiment.toRMP());
        System.out.println(experiment.toPrettyPFA());
        assertTrue(!experiment.toPFA().contains("error"));
        assertTrue(experiment.toPFA().contains("\"model\""));
        assertTrue(experiment.toPFA().contains("\"action\""));
    }

    @Test
    public void test_regression() throws Exception {

        String[] featureNames = new String[]{"input1", "input2"};
        String variableName = "output";
        double[][] data = new double[][]{
                {1.2, 2.4},
                {6.7, 8.9},
                {4.6, 23.4},
                {7.6, 5.4},
                {1.2, 1.6},
                {3.4, 4.7},
                {3.4, 6.5}
        };
        double[] labels = new double[]{2.4, 4.5, 5.7, 4.5, 23.7, 8.7, 9.2};

        // Get experiment input
        RegressionInputData input = new RegressionInputData(featureNames, variableName, data, labels);
        RapidMinerModel model = new RPMDefault("median");

        // Run experiment
        final RapidMinerAlgorithmSerializer serializer = new RapidMinerAlgorithmSerializer(new RPMDefaultSerializer());
        final RapidMinerExperiment experiment = new RapidMinerExperiment(input, model, serializer);
        experiment.run();

        System.out.println(experiment.toRMP());
        System.out.println(experiment.toPrettyPFA());
        assertTrue(!experiment.toPFA().contains("error"));
        assertTrue(experiment.toPFA().contains("\"model\""));
        assertTrue(experiment.toPFA().contains("\"action\""));
    }
}
