package eu.humanbrainproject.mip.algorithms.weka;

import com.weka.example.Attribute;
import com.weka.example.ExampleSet;
import com.weka.example.table.AttributeFactory;
import com.weka.example.table.DoubleArrayDataRow;
import com.weka.example.table.MemoryExampleTable;
import com.weka.tools.Ontology;
import eu.humanbrainproject.mip.algorithms.db.DBException;
import eu.humanbrainproject.mip.algorithms.weka.InputData;

import java.util.LinkedList;
import java.util.List;

/**
 * Useful for tests
 */
public class ClassificationInputData extends InputData {

    private final double[][] sampleData;
    private final String[] labels;

    public ClassificationInputData(String[] featuresNames, String variableName, double[][] sampleData, String[] labels) {
        super(featuresNames, variableName, null);

        this.sampleData = sampleData;
        this.labels = labels;
    }

    protected ExampleSet createExampleSet() throws DBException {

        List<Attribute> attributes = new LinkedList<>();
        for (String featuresName : getFeaturesNames()) {
            attributes.add(AttributeFactory.createAttribute(featuresName, Ontology.REAL));
        }

        // Create label
        Attribute label = AttributeFactory.createAttribute(getVariableName(), Ontology.NOMINAL);
        attributes.add(label);

        // Create table
        MemoryExampleTable table = new MemoryExampleTable(attributes);

        // Fill the table
        for (int d = 0; d < sampleData.length; d++) {
            double[] tableData = new double[attributes.size()];
            System.arraycopy(sampleData[d], 0, tableData, 0, sampleData[d].length);

            // Maps the nominal classification to a double value
            tableData[sampleData[d].length] = label.getMapping().mapString(labels[d]);

            // Add data row
            table.addDataRow(new DoubleArrayDataRow(tableData));
        }

        // Create example set
        return table.createExampleSet(label);
    }
}
