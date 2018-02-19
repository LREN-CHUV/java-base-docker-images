
package eu.humanbrainproject.mip.algorithms.jsi;

import java.net.URL;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import eu.humanbrainproject.mip.algorithms.jsi.common.ClusAlgorithm;
import eu.humanbrainproject.mip.algorithms.jsi.common.ClusMeta;
import eu.humanbrainproject.mip.algorithms.jsi.dummy.DummyMeta;
import eu.humanbrainproject.mip.algorithms.jsi.dummy.DummySerializer;
import eu.humanbrainproject.mip.algorithms.jsi.dummy.FileInputData;
import eu.humanbrainproject.mip.algorithms.jsi.serializers.pfa.ClusGenericSerializer;
import eu.humanbrainproject.mip.algorithms.jsi.serializers.pfa.ClusModelPFASerializer;
import si.ijs.kt.clus.model.ClusModel;

import org.codehaus.jackson.JsonNode;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


@DisplayName("With CLUS algorithms")
public class MainTest {

    private static ObjectMapper mapper = new ObjectMapper();


    @Test
    @DisplayName("we can implement a dummy regressor and export its model to PFA")
    public void testRegression() throws Exception {

        String[] featureNames = new String[] { "input1", "input2" };
        String[] variableNames = new String[] { "output1" };

        // Get algorithm input
        final URL resource = getClass().getResource("regression.csv");
        assertNotNull(resource);

        FileInputData input = new FileInputData(featureNames, variableNames, resource, ".csv", 0);

        ClusMeta clusMeta = new DummyMeta();
        ClusGenericSerializer<ClusModel> modelSerializer = new DummySerializer();
        ClusModelPFASerializer<ClusModel> mainSerializer = new ClusModelPFASerializer<>(modelSerializer);
        ClusAlgorithm<ClusModel> algorithm = new ClusAlgorithm<>(input, mainSerializer, clusMeta);

        algorithm.run();

        System.out.println(algorithm.toPrettyPFA());
        String pfa = algorithm.toPFA();
        assertTrue(!pfa.contains("error"));
        assertTrue(pfa.contains("\"action\""));
        assertTrue(pfa.contains("\"Hello\":\"World\""));

        final JsonNode jsonPfa = mapper.readTree(pfa.replaceFirst("SELECT \\* FROM .*\\\\\"", "SELECT"));
        final JsonNode jsonExpected = mapper.readTree(getClass().getResource("regression.pfa.json"));

        assertEquals(jsonExpected, jsonPfa);
    }
}