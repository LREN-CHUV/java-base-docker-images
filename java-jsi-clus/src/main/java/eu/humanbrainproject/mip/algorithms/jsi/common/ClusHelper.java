
package eu.humanbrainproject.mip.algorithms.jsi.common;

import java.io.File;
import java.io.IOException;


import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.google.common.io.Files;


public class ClusHelper {

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

        /** where should the JSON PFA file be stored */
        public static final String CLUS_PFAFILE = CLUS_FILE + ".json";

        public static final String CLUS_DEBUG_ENV_VARIABLE = "CLUS_DEBUG";
        public static final String CLUS_ALGORITHM_TYPE_ENV_VARIABLE = "CLUS_ALGORITHM_TYPE";
    }


    public static void writeDebug(String pfa) throws IOException {
        File sourceFile = new File("/usr/share/jars/" + ClusConstants.CLUS_SETTINGSFILE);
        File destinationFile = new File("/host-data/out/jars/" + ClusConstants.CLUS_SETTINGSFILE);
        Files.copy(sourceFile, destinationFile);
        
        sourceFile = new File("/usr/share/jars/" + ClusConstants.CLUS_DATAFILE);
        destinationFile = new File("/host-data/out/jars/" + ClusConstants.CLUS_DATAFILE);
        Files.copy(sourceFile, destinationFile);
        
        sourceFile = new File("/usr/share/jars/" + ClusConstants.CLUS_OUTFILE);
        destinationFile = new File("/host-data/out/jars/" + ClusConstants.CLUS_OUTFILE);
        Files.copy(sourceFile, destinationFile);

        sourceFile = new File("/usr/share/jars/" + ClusConstants.CLUS_MODELFILE);
        destinationFile = new File("/host-data/out/jars/" + ClusConstants.CLUS_MODELFILE);
        Files.copy(sourceFile, destinationFile);

        File pfaFile = new File(ClusConstants.CLUS_PFAFILE);
        JsonFactory fact = new JsonFactory();
        JsonGenerator gen = fact.createGenerator(pfaFile, JsonEncoding.UTF8);
        gen.writeString(pfa);
        gen.close();

        sourceFile = new File("/usr/share/jars/" + ClusConstants.CLUS_PFAFILE);
        destinationFile = new File("/host-data/out/jars/" + ClusConstants.CLUS_PFAFILE);
        Files.copy(sourceFile, destinationFile);
    }
}
