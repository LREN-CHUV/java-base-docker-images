package eu.humanbrainproject.mip.algorithms.weka;

import com.opendatagroup.hadrian.jvmcompiler.PFAEngine;
import com.opendatagroup.hadrian.jvmcompiler.PFAEngine$;
import eu.humanbrainproject.mip.algorithms.weka.serializers.pfa.WekaAlgorithmSerializer;
import eu.humanbrainproject.mip.algorithms.weka.simplelr.SimpleLinearRegressionSerializer;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import scala.Option;
import scala.collection.immutable.HashMap;
import weka.classifiers.functions.SimpleLinearRegression;

import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for WekaAlgorithm
 *
 * @author Arnaud Jutzeler
 */
@DisplayName("With Weka algorithms")
public class WekaAlgorithmTest {

    private static ObjectMapper mapper = new ObjectMapper();

    @Test
    @DisplayName("we can implement a linear regression and export its model to PFA")
    public void testRegression() throws Exception {

        String[] featureNames = new String[]{"input1", "input2"};
        String variableName = "output";

        // Get algorithm input
        final URL resource = getClass().getResource("regression.csv");
        assertNotNull(resource);

        FileInputData input = new FileInputData(featureNames, variableName, resource, ".csv");
        WekaClassifier<SimpleLinearRegression> classifier = new WekaClassifier<>(SimpleLinearRegression.class);

        // Run algorithm
        final WekaAlgorithmSerializer<SimpleLinearRegression> serializer = new WekaAlgorithmSerializer<>(new SimpleLinearRegressionSerializer());
        final WekaAlgorithm<SimpleLinearRegression> algorithm = new WekaAlgorithm<>(input, classifier, serializer);
        algorithm.run();

        System.out.println(algorithm.toPrettyPFA());
        String pfa = algorithm.toPFA();
        assertTrue(!pfa.contains("error"));
        assertTrue(pfa.contains("\"model\""));
        assertTrue(pfa.contains("\"action\""));

        final JsonNode jsonPfa = mapper.readTree(pfa);
        final JsonNode jsonExpected = mapper.readTree(getClass().getResource("regression.pfa.json"));

        assertEquals(jsonExpected, jsonPfa);

        // Validate PFA
        PFAEngine<Object, Object> pfaEngine = getPFAEngine(pfa);

        // Execute the PFA predictive algorithm on new data to make a prediction
        Object result = pfaEngine.action(pfaEngine.jsonInput("{\"input1\": {\"double\": 1.1}, \"input2\": {\"double\": 2.0}}"));
        assertEquals(3.9112, (Double)result, 0.001);
    }

    private PFAEngine<Object, Object> getPFAEngine(String pfa) {
        return PFAEngine$.MODULE$.fromJson(pfa, new HashMap<>(), "0.8.1", Option.empty(),
                1, Option.empty(), false).head();
    }

}
