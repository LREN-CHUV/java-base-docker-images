
package eu.humanbrainproject.mip.algorithms.jsi.common;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import eu.humanbrainproject.mip.algorithms.Algorithm;
import eu.humanbrainproject.mip.algorithms.Configuration;
import eu.humanbrainproject.mip.algorithms.db.DBException;
import eu.humanbrainproject.mip.algorithms.jsi.serializers.pfa.ClusModelPFASerializer;
import eu.humanbrainproject.mip.algorithms.jsi.serializers.pfa.ClusVisualizationSerializer;
import si.ijs.kt.clus.model.ClusModel;
import si.ijs.kt.clus.model.io.ClusModelCollectionIO;
import weka.core.Instances;
import weka.core.converters.ArffSaver;

public class ClusAlgorithm<M extends ClusModel> implements Algorithm {

	private static final Logger LOGGER = Logger.getLogger(ClusAlgorithm.class.getName());

	private final ClusMeta clusMeta;
	private M model; // Induced model
	private ClusModelPFASerializer<M> algorithmSerializer; // Serializer for the
															// algorithm
	private Exception exception; // Exception (if applicable)
	private InputData input;

	@Override
	public Set<AlgorithmCapability> getCapabilities() {
		return clusMeta.CAPABILITIES;
	}

	@Override
	public String getDocumentation() {
		return clusMeta.DOCUMENTATION;
	}

	@Override
	public String getName() {
		return clusMeta.NAME;
	}

	@Override
	public String getErrorMessage() {
		if (exception != null) {
			return exception.getMessage();
		} else {
			return null;
		}
	}

	public M getModel() {
		return model;
	}

	public ClusAlgorithm(ClusModelPFASerializer<M> serializer, ClusMeta clusMeta) {
		this.algorithmSerializer = serializer;
		this.clusMeta = clusMeta;
	}

	public InputData getInput() {
		return input;
	}

	@SuppressWarnings("unchecked")
	public void run() throws Exception {

		// write data and settings do disk
		writeArffAndSettings();

		// Run experiment
		clusMeta.CMDLINE_SWITCHES.add(ClusHelper.ClusConstants.CLUS_SETTINGSFILE);
		si.ijs.kt.clus.Clus.main(clusMeta.CMDLINE_SWITCHES.toArray(new String[0]));

		// save model
		ClusModelCollectionIO io = ClusModelCollectionIO.load(ClusHelper.ClusConstants.CLUS_MODELFILE);
		model = (M) io.getModel(clusMeta.WHICH_MODEL_TO_USE);
		
	}

	/**
	 * Generate the PFA representation of the experiment outcome
	 *
	 * @return
	 * @throws IOException
	 */
	public String toPrettyPFA() throws IOException {
		ObjectMapper myObjectMapper = getObjectMapper();
		return myObjectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
	}

	public String toPFA() throws IOException {
		ObjectMapper myObjectMapper = getObjectMapper();
		return myObjectMapper.writeValueAsString(this);
	}

	@SuppressWarnings("unchecked")
	private ObjectMapper getObjectMapper() {
		ObjectMapper myObjectMapper = new ObjectMapper();
		SimpleModule module = new SimpleModule("CLUS", new Version(1, 0, 0, null, null, null));
		// module.addSerializer(algorithmSerializer);

		module.addSerializer((Class<? extends ClusAlgorithm<M>>) this.getClass(), algorithmSerializer);

		myObjectMapper.registerModule(module);
		return myObjectMapper;
	}

	/**
	 * Fetches data from database and saves it to arff. It also creates the
	 * settings file for CLUS.
	 */
	private void writeArffAndSettings() {
		try {
			// get data and save it to disk
			input = InputData.fromEnv();
			Instances data = input.getData();

			ArffSaver arffSaver = new ArffSaver();
			arffSaver.setInstances(data);
			arffSaver.setFile(new File(ClusHelper.ClusConstants.CLUS_DATAFILE));
			arffSaver.writeBatch();

			// .s file
			List<String> descriptiveIndices = new ArrayList<String>();
			List<String> targetIndices = new ArrayList<String>();

			for (String s : Configuration.INSTANCE.covariables()) {
				descriptiveIndices.add(data.attribute(s).index() + 1 + "");
			}

			for (String s : Configuration.INSTANCE.variables()) {
				targetIndices.add(data.attribute(s).index() + 1 + "");
			}

			Map<String, String> generalDict = new HashMap<>();
			Double myDouble = Configuration.INSTANCE.randomSeed();
			Integer seed = myDouble == null ? 1 : Integer.valueOf((int) Math.round(myDouble));
			generalDict.put("RandomSeed", seed.toString());
			generalDict.put("Verbose", "0");

			Map<String, String> dataDict = new HashMap<>();
			dataDict.put("File", ClusHelper.ClusConstants.CLUS_DATAFILE);

			Map<String, String> attributesDict = new HashMap<>();
			attributesDict.put("Descriptive", String.join(",", descriptiveIndices));
			attributesDict.put("Target", String.join(",", targetIndices));

			Map<String, String> outputDict = new HashMap<>();
			outputDict.put("WriteModelFile", "Yes");
			outputDict.put("TrainErrors", "Yes");

			addSafe("[General]", generalDict);
			addSafe("[Data]", dataDict);
			addSafe("[Attributes]", attributesDict);
			addSafe("[Output]", outputDict);

			PrintWriter pw = new PrintWriter(new FileWriter(ClusHelper.ClusConstants.CLUS_SETTINGSFILE));
			for (String s : clusMeta.SETTINGS.keySet()) {
				pw.write(s + System.lineSeparator());

				Map<String, String> m = clusMeta.SETTINGS.get(s);
				for (String e : m.keySet()) {
					pw.write(e + "=" + m.get(e) + System.lineSeparator());
				}
			}

			pw.flush();
			pw.close();
		} catch (IOException | DBException e) {
			LOGGER.severe(e.getMessage());
			System.exit(1);
		}
	}

	private void addSafe(String key, Map<String, String> entries) {
		Map<String, Map<String, String>> x = clusMeta.SETTINGS;
		if (!x.containsKey(key)) {
			x.put(key, entries);
		} else {
			Map<String, String> existing = x.get(key);

			for (String s : entries.keySet()) {
				if (existing.putIfAbsent(s, entries.get(s)) != null) { // key
																		// exists
																		// and
																		// has
																		// value
					// override the current value
					existing.put(s, entries.get(s));
				}
			}
		}
	}
}
