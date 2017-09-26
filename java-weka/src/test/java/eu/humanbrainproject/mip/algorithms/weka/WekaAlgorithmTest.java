package eu.humanbrainproject.mip.algorithms.weka;

import com.opendatagroup.hadrian.jvmcompiler.PFAEngine;
import com.opendatagroup.hadrian.jvmcompiler.PFAEngine$;
import com.weka.operator.learner.lazy.DefaultModel;
import eu.humanbrainproject.mip.algorithms.weka.models.WekaModel;
import eu.humanbrainproject.mip.algorithms.weka.rpmdefault.RPMDefault;
import eu.humanbrainproject.mip.algorithms.weka.rpmdefault.RPMDefaultSerializer;
import eu.humanbrainproject.mip.algorithms.weka.serializers.pfa.RapidMinerAlgorithmSerializer;
import org.junit.Test;
import scala.Option;
import scala.collection.immutable.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests for WekaAlgorithm
 *
 * @author Arnaud Jutzeler
 */
public class WekaAlgorithmTest {

    @Test
    public void testClassification() throws Exception {

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
        WekaModel<DefaultModel> model = new RPMDefault("mode");

        // Run experiment
        final RapidMinerAlgorithmSerializer<DefaultModel> serializer = new RapidMinerAlgorithmSerializer<>(new RPMDefaultSerializer());
        final WekaAlgorithm<DefaultModel> experiment = new WekaAlgorithm<>(input, model, serializer);
        experiment.run();

        System.out.println(experiment.toRMP());
        System.out.println(experiment.toPrettyPFA());
        String pfa = experiment.toPFA();
        assertTrue(!pfa.contains("error"));
        assertTrue(pfa.contains("\"model\""));
        assertTrue(pfa.contains("\"action\""));

        // Validate PFA
        PFAEngine<Object, Object> pfaEngine = getPFAEngine(pfa);

        Object result = pfaEngine.action(pfaEngine.jsonInput("{\"input1\": 1.1, \"input2\": 2.0}"));
        assertEquals("1.0", result);
    }

    @Test
    public void testRegression() throws Exception {

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
        WekaModel<DefaultModel> model = new RPMDefault("median");

        // Run experiment
        final RapidMinerAlgorithmSerializer<DefaultModel> serializer = new RapidMinerAlgorithmSerializer<>(new RPMDefaultSerializer());
        final WekaAlgorithm<DefaultModel> experiment = new WekaAlgorithm<>(input, model, serializer);
        experiment.run();

        System.out.println(experiment.toRMP());
        System.out.println(experiment.toPrettyPFA());

        String pfa = experiment.toPFA();
        assertTrue(!pfa.contains("error"));
        assertTrue(pfa.contains("\"model\""));
        assertTrue(pfa.contains("\"action\""));

        // Validate PFA
        PFAEngine<Object, Object> pfaEngine = getPFAEngine(pfa);

        Object result = pfaEngine.action(pfaEngine.jsonInput("{\"input1\": 1.1, \"input2\": 2.0}"));
        assertEquals(5.7, result);

    }

    @Test
    public void testAttribute() throws Exception {

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
        WekaModel<DefaultModel> model = new RPMDefault("median");

        // Run experiment
        final RapidMinerAlgorithmSerializer<DefaultModel> serializer = new RapidMinerAlgorithmSerializer<>(new RPMDefaultSerializer());
        final WekaAlgorithm<DefaultModel> experiment = new WekaAlgorithm<>(input, model, serializer);
        experiment.run();

        System.out.println(experiment.toRMP());
        System.out.println(experiment.toPrettyPFA());

        String pfa = experiment.toPFA();
        assertTrue(!pfa.contains("error"));
        assertTrue(pfa.contains("\"model\""));
        assertTrue(pfa.contains("\"action\""));

        // Validate PFA
        PFAEngine<Object, Object> pfaEngine = getPFAEngine(pfa);

        Object result = pfaEngine.action(pfaEngine.jsonInput("{\"input1\": 1.1, \"input2\": 2.0}"));
        assertEquals(5.7, result);

    }

    private PFAEngine<Object, Object> getPFAEngine(String pfa) {
        return PFAEngine$.MODULE$.fromJson(pfa, new HashMap<>(), "0.8.1", Option.empty(),
                1, Option.empty(), false).head();
    }

}
