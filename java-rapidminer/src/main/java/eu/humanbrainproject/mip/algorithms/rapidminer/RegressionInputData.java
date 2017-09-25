package eu.humanbrainproject.mip.algorithms.rapidminer;

import com.rapidminer.example.Attribute;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.example.table.AttributeFactory;
import com.rapidminer.example.table.DoubleArrayDataRow;
import com.rapidminer.example.table.MemoryExampleTable;
import com.rapidminer.tools.Ontology;
import eu.humanbrainproject.mip.algorithms.db.DBException;
import eu.humanbrainproject.mip.algorithms.rapidminer.InputData;

import java.util.LinkedList;
import java.util.List;

/**
 * Useful for tests
 */
public class RegressionInputData extends InputData {

    private final double[][] sampleData;
    private final double[] labels;

    public RegressionInputData(String[] featuresNames, String variableName, double[][] sampleData, double[] labels) {
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
        Attribute label = AttributeFactory.createAttribute(getVariableName(), Ontology.REAL);
        attributes.add(label);

        // Create table
        MemoryExampleTable table = new MemoryExampleTable(attributes);

        // Fill the table
        for (int d = 0; d < sampleData.length; d++) {
            double[] tableData = new double[attributes.size()];
            System.arraycopy(sampleData[d], 0, tableData, 0, sampleData[d].length);

            tableData[sampleData[d].length] = labels[d];

            // Add data row
            table.addDataRow(new DoubleArrayDataRow(tableData));
        }

        // Create example set
        return table.createExampleSet(label);
    }
}
