
package eu.humanbrainproject.mip.algorithms.jsi.common;

/**
 * @author Martin Breskvar
 * 
 *         A class for storing some simple constants
 */
public class ClusConstants {

    /** the name of all files regarding clus (this can be changed) */
    private static final String CLUS_FILE = "experiment";

    /** where should the arff be stored (clus specific settings, do not change this!) */
    public static final String CLUS_DATAFILE = CLUS_FILE + ".arff";

    /** where should the settings file be stored (clus specific settings, do not change this!) */
    public static final String CLUS_SETTINGSFILE = CLUS_FILE + ".s";

    /** where should the .out file be stored (clus specific settings, do not change this!) */
    public static final String CLUS_OUTFILE = CLUS_FILE + ".out";

    /** where should the .model file be stored (clus specific settings, do not change this!) */
    public static final String CLUS_MODELFILE = CLUS_FILE + ".model";

}
