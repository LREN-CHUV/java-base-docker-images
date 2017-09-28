package eu.humanbrainproject.mip.algorithms.weka;

import weka.core.Attribute;
import weka.core.Instances;
import weka.core.converters.AbstractFileLoader;
import weka.core.converters.ConverterUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;

public class FileInputData extends InputData {

    final AbstractFileLoader loader;
    final String source;

    public FileInputData(String[] featuresNames, String variableName, File file) {
        super(featuresNames, variableName, null);
        source = file.getAbsolutePath();
        loader = ConverterUtils.getLoaderForFile(file);
    }

    public FileInputData(String[] featuresNames, String variableName, URL remoteFile, String extension) throws IOException {
        super(featuresNames, variableName, null);
        source = remoteFile.toExternalForm();
        loader = ConverterUtils.getURLLoaderForExtension(extension);
        loader.setSource(remoteFile.openStream());
    }

    public String getQuery() {
        return "SELECT * FROM \"" + source + "\"";
    }

    /**
     * Get the data from DB
     */
    protected Instances createInstances() {
        try {
            final Instances dataSet = loader.getDataSet();
            for (Attribute attribute: Collections.list(dataSet.enumerateAttributes())) {
                if (attribute.name().equals(getVariableName())) {
                    dataSet.setClassIndex(attribute.index());
                    break;
                }
            }
            return dataSet;
        } catch (IOException e) {
            throw new RuntimeException("Cannot load data from " + source, e);
        }
    }
}
