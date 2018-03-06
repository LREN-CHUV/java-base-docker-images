
package eu.humanbrainproject.mip.algorithms.jsi;

import java.util.Map;
import java.util.logging.Logger;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Files;

import eu.humanbrainproject.mip.algorithms.jsi.common.ClusAlgorithm;
import eu.humanbrainproject.mip.algorithms.jsi.common.ClusMeta;
import eu.humanbrainproject.mip.algorithms.jsi.common.InputData;
import eu.humanbrainproject.mip.algorithms.jsi.serializers.pfa.ClusDescriptiveSerializer;
import eu.humanbrainproject.mip.algorithms.jsi.serializers.pfa.ClusModelPFASerializer;
import eu.humanbrainproject.mip.algorithms.jsi.serializers.pfa.ClusVisualizationSerializer;
import eu.humanbrainproject.mip.algorithms.jsi.common.ClusConstants;
import eu.humanbrainproject.mip.algorithms.jsi.common.ClusHelpers;
import si.ijs.kt.clus.algo.rules.ClusRuleSet;
import si.ijs.kt.clus.ext.featureRanking.Fimp;
import si.ijs.kt.clus.model.ClusModel;

import eu.humanbrainproject.mip.algorithms.Configuration;
import eu.humanbrainproject.mip.algorithms.ResultsFormat;
import eu.humanbrainproject.mip.algorithms.Algorithm.AlgorithmCapability;
import eu.humanbrainproject.mip.algorithms.db.OutputDataConnector;

/**
 * Entry point
 *
 * @author Martin Breskvar
 */
public class Main<M extends ClusModel> {

  private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

  private ClusModelPFASerializer<M> algorithmSerializer = null;
  private ClusMeta clusMeta = null;
  private ClusVisualizationSerializer<M> algorithmVisualizationSerializer = null;
  private ClusDescriptiveSerializer algorithmDescriptiveSerializer = null;

  public Main(
      ClusModelPFASerializer<M> algorithmSerializer,
      ClusMeta clusMeta,
      ClusVisualizationSerializer<M> algorithmVisualizationSerializer,
      ClusDescriptiveSerializer algorithmDescriptiveSeriRalizer) {

    if (algorithmSerializer == null) {
      throw new IllegalArgumentException("algorithmSerializer is null");
    }
    if (clusMeta == null) {
      throw new IllegalArgumentException("clusMeta is null");
    }

    this.algorithmSerializer = algorithmSerializer;
    this.algorithmVisualizationSerializer = algorithmVisualizationSerializer;
    this.clusMeta = clusMeta;
    this.algorithmDescriptiveSerializer = algorithmDescriptiveSeriRalizer;
  }

  public Main(ClusModelPFASerializer<M> algorithmSerializer, ClusMeta clusMeta) {
    this(algorithmSerializer, clusMeta, null, null);
  }

  public Main(
      ClusModelPFASerializer<M> algorithmSerializer,
      ClusMeta clusMeta,
      ClusVisualizationSerializer<M> algorithmVisualizationSerializer) {
    this(algorithmSerializer, clusMeta, algorithmVisualizationSerializer, null);
  }

  public Main(
      ClusModelPFASerializer<M> algorithmSerializer,
      ClusMeta clusMeta,
      ClusDescriptiveSerializer algorithmDescriptiveSeriRalizer) {
    this(algorithmSerializer, clusMeta, null, algorithmDescriptiveSeriRalizer);
  }

  public void run() throws Exception {

    LOGGER.finest("Starting the CLUS library");
    LOGGER.finest("Preparing WEKA database props");

    // weka properties
    Path targetDbProps =
        FileSystems.getDefault()
            .getPath("/opt", "weka", "props", "weka", "experiment", "DatabaseUtils.props");
    if (Configuration.INSTANCE.inputJdbcUrl().startsWith("jdbc:postgresql:")) {
      Path dbProps =
          FileSystems.getDefault()
              .getPath("/opt", "weka", "databases-props", "DatabaseUtils.props.postgresql");
      Files.createLink(targetDbProps, dbProps);
    }

    LOGGER.finest("Reading input data");
    InputData input = InputData.fromEnv();

    ClusAlgorithm<M> experiment = new ClusAlgorithm<>(input, algorithmSerializer, clusMeta);

    String visualizationOutput = "";
    String descriptiveOutput = "";
    String tabularDataResourceOutput = "";
    OutputDataConnector out = null;
    try {
      LOGGER.info("Starting experiment");
      experiment.run();

      out = OutputDataConnector.fromEnv();

      // generate vizualization if it exists
      if (algorithmVisualizationSerializer != null
          && experiment.getCapabilities().contains(AlgorithmCapability.VISUALISATION)) {

        LOGGER.info("Reading visualization");
        
        visualizationOutput =
            algorithmVisualizationSerializer.getVisualizationString(experiment.getModel(), experiment.getInput().getOutputFeaturesNames());
      }

      if (algorithmDescriptiveSerializer != null) {
        if (experiment.getCapabilities().contains(AlgorithmCapability.FEATURE_IMPORTANCE)
            && !experiment.getCapabilities().contains(AlgorithmCapability.PREDICTIVE_MODEL)) {

          // get feature importances if they exist
          if (clusMeta.SETTINGS.containsKey("[Ensemble]")) {
            LOGGER.info("Reading feature importances");

            Map<String, String> ensembleSettings = clusMeta.SETTINGS.get("[Ensemble]");
            String iterations = ensembleSettings.get("Iterations");
            String fimpFile = ClusConstants.CLUS_FILE + iterations + ".fimp";

            if (new File(fimpFile).exists()) {
              si.ijs.kt.clus.ext.featureRanking.Fimp fimp = new Fimp(fimpFile);
              tabularDataResourceOutput = algorithmDescriptiveSerializer.getFimpString(fimp);
            }
          }
        } else if (experiment.getCapabilities().contains(AlgorithmCapability.PREDICTIVE_MODEL)) {
          // we have a descriptive output for the algorithm
          // currently we only support rule models
          if (experiment.getModel() instanceof ClusRuleSet) {

            LOGGER.info("Reading descriptive output");

            descriptiveOutput =
                algorithmDescriptiveSerializer.getRuleSetString(
                    (ClusRuleSet) experiment.getModel());
          }
        }
      }
    } catch (Exception e) {
      LOGGER.log(java.util.logging.Level.SEVERE, "Cannot execute the algorithm", e);
      System.exit(1);
    } finally {
      // if algorithm is predictive, get constructed PFA and save it to database
      if (experiment.getCapabilities().contains(AlgorithmCapability.PREDICTIVE_MODEL)) {
        String pfa = experiment.toPFA();

        LOGGER.finest("Saving PFA to database");
        // Write results PFA in DB - it can represent also an error
        out.saveResults(pfa, ResultsFormat.PFA_JSON);
      }

      // if algorithm has a visualization
      if (visualizationOutput != "") {
        LOGGER.finest("Saving VISUALIZATION to database");
        out.saveResults(visualizationOutput, ResultsFormat.JAVASCRIPT_VISJS);
      }

      // if algorithm produced tabular data
      if (tabularDataResourceOutput != "") {
        LOGGER.finest("Saving TABULAR DATA to database");
        out.saveResults(tabularDataResourceOutput, ResultsFormat.TABULAR_DATA_RESOURCE_JSON);
      }

      // if algorithm produced HTML output
      if (descriptiveOutput != "") {
        LOGGER.finest("Saving DESCRIPTIVE OUTPUT to database");
        out.saveResults(descriptiveOutput, ResultsFormat.HTML);
      }

      // clean up
      ClusHelpers.CleanUp();
    }
  }
}
