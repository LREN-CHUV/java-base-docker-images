
package eu.humanbrainproject.mip.algorithms.jsi;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import eu.humanbrainproject.mip.algorithms.jsi.common.ClusAlgorithm;
import eu.humanbrainproject.mip.algorithms.jsi.common.ClusMeta;
import eu.humanbrainproject.mip.algorithms.jsi.common.ClusConstants;
import eu.humanbrainproject.mip.algorithms.jsi.dummy.DummyMeta;
import eu.humanbrainproject.mip.algorithms.jsi.dummy.DummyModelSerializer;
import eu.humanbrainproject.mip.algorithms.jsi.dummy.DummyVisualizer;
import eu.humanbrainproject.mip.algorithms.jsi.dummy.FileInputData;
import eu.humanbrainproject.mip.algorithms.jsi.serializers.pfa.ClusGenericSerializer;
import eu.humanbrainproject.mip.algorithms.jsi.serializers.pfa.ClusModelPFASerializer;
import si.ijs.kt.clus.model.ClusModel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;


/**
 * 
 * @author Martin Breskvar
 *
 */

@DisplayName("With CLUS algorithms")
public class MainTest {

    private static ObjectMapper mapper = new ObjectMapper();


    @Test
    @DisplayName("we must provide non-null arguments to the ClusAlgorithm constructor")
    public void testInputArguments() throws Exception {
        assertThrows(IllegalArgumentException.class, () -> new ClusAlgorithm<>(null, null, null));
    }


    @Test
    @DisplayName("some files will be generated on the fly")
    public void testCreateFiles() throws Exception {
        String[] featureNames = new String[] { "input1", "input2" };
        String[] variableNames = new String[] { "output1" };

        final URL resource = getClass().getResource("data.csv");
        assertNotNull(resource);

        FileInputData input = new FileInputData(featureNames, variableNames, resource, ".csv", 0);

        ClusMeta clusMeta = new DummyMeta();
        ClusGenericSerializer<ClusModel> modelSerializer = new DummyModelSerializer();
        ClusModelPFASerializer<ClusModel> mainSerializer = new ClusModelPFASerializer<>(modelSerializer);
        ClusAlgorithm<ClusModel> algorithm = new ClusAlgorithm<>(input, mainSerializer, clusMeta);

        algorithm.run();

        ArrayList<File> files = new ArrayList<File>();

        files.add(new File(ClusConstants.CLUS_DATAFILE));
        files.add(new File(ClusConstants.CLUS_MODELFILE));
        files.add(new File(ClusConstants.CLUS_OUTFILE));
        files.add(new File(ClusConstants.CLUS_SETTINGSFILE));

        for (File f : files) {
            System.out.println("Testing existance of: " + f.getName());
            assertTrue(f.exists());
        }

        System.out.println(algorithm.toPrettyPFA());
        String pfa = algorithm.toPFA();

        assertTrue(!pfa.contains("error"));
        assertTrue(pfa.contains("\"action\""));
        assertTrue(pfa.contains("DummyAlgorithm"));

        final JsonNode jsonPfa = mapper.readTree(pfa.replaceFirst("SELECT \\* FROM .*\\\\\"", "SELECT"));
        final JsonNode jsonExpected = mapper.readTree(getClass().getResource("dummy.pfa.json"));

        assertEquals(jsonExpected, jsonPfa);
    }
    
    
    @Test
    @DisplayName("some algorithms may produce visualizations")
    public void testVisualization() {
        DummyVisualizer v = new DummyVisualizer();
        
        assertEquals("DummyVisualization", v.getVisualizationString(null));
    }
}