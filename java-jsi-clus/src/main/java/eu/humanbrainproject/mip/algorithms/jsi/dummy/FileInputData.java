
package eu.humanbrainproject.mip.algorithms.jsi.dummy;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import eu.humanbrainproject.mip.algorithms.jsi.common.InputData;
import weka.core.Instances;
import weka.core.converters.AbstractFileLoader;
import weka.core.converters.ConverterUtils;


/**
 * @return the input data initialized from a file
 */
public class FileInputData extends InputData {

    final AbstractFileLoader loader;
    final String source;


    public FileInputData(String[] featuresNames, String[] variableNames, File file, int randomSeed) {
        super(featuresNames, variableNames, null, randomSeed);
        source = file.getAbsolutePath();
        loader = ConverterUtils.getLoaderForFile(file);
    }

    public FileInputData(String[] featuresNames, String[] variableNames, URL remoteFile, String extension, int randomSeed) throws IOException {
        super(featuresNames, variableNames, null, randomSeed);
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

            return dataSet;
        }
        catch (IOException e) {
            throw new RuntimeException("Cannot load data from " + source, e);
        }
    }
}