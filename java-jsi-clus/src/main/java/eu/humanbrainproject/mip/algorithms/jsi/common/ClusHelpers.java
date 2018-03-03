package eu.humanbrainproject.mip.algorithms.jsi.common;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;

public class ClusHelpers {
  public static void CleanUp() {
    // remove all temp files
    ArrayList<String> extensions =
        new ArrayList<String>(Arrays.asList("s", "arff", "out", "fimp", "model"));

    for (String extension : extensions) {

      File[] files = getFiles(".", extension);

      for (File f : files) {
        try {
          f.delete();
        } catch (Exception ex) {
          /* suppress */
        }
      }
    }
  }

  private static File[] getFiles(String dirName, String extension) {
    File dir = new File(dirName);

    FilenameFilter filter =
        new FilenameFilter() {
          public boolean accept(File dir, String filename) {
            return filename.endsWith("." + extension);
          }
        };

    return dir.listFiles(filter);
  }
}
