package cz.nkp.differ.compare.metadata.external;

import cz.nkp.differ.compare.metadata.AbstractMetadataExtractor;
import cz.nkp.differ.compare.metadata.ImageMetadata;
import cz.nkp.differ.compare.metadata.MetadataExtractorWithAttributes;
import cz.nkp.differ.compare.metadata.MetadataSource;
import cz.nkp.differ.compare.metadata.external.ResultTransformer.Entry;
import cz.nkp.differ.plugins.tools.CommandRunner;
import cz.nkp.differ.plugins.tools.CommandRunner.CommandOutput;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author xrosecky
 */
public class ExternalMetadataExtractor extends AbstractMetadataExtractor
		implements MetadataExtractorWithAttributes {

	private static final String VERSION_PROPERTY = "version of extractor";

	Logger logger = LogManager.getLogger(ExternalMetadataExtractor.class);

	@Autowired
	private CommandRunner commandRunner;

	private List<String> programArguments;
	private ResultTransformer transformer;
	private String source;
	private Map<String, String> units;

	public List<String> getProgramArguments() {
		return programArguments;
	}

	public void setProgramArguments(List<String> programArguments) {
		this.programArguments = programArguments;
	}

	public ResultTransformer getTransformer() {
		return transformer;
	}

	public void setTransformer(ResultTransformer transformer) {
		this.transformer = transformer;
	}

	@Override
	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Map<String, String> getUnits() {
		return units;
	}

	public void setUnits(Map<String, String> units) {
		this.units = units;
	}

	@Override
	public List<ImageMetadata> getMetadata(File file) {
		if (super.isSupported(file)) {
			return this.getMetadata(Collections.singletonMap("{file}",
					file.getAbsolutePath()));
		} else {
			return Collections.emptyList();
		}
	}

	public List<ImageMetadata> getMetadata(Map<String, String> attributes) {
		List<ImageMetadata> result = new ArrayList<ImageMetadata>();
		List<String> arguments = new ArrayList<String>();
		for (String argument : programArguments) {
			if (attributes.containsKey(argument)) {
				arguments.add(attributes.get(argument));
			} else {
				arguments.add(argument);
			}
		}
		try {
			CommandOutput cmdResult = commandRunner.runCommandAndWaitForExit(
					null, arguments);
			String stdout = null;
			if (cmdResult.getStdout() != null) {
				stdout = new String(cmdResult.getStdout());
			}
			String stderr = null;
			if (cmdResult.getStderr() != null) {
				stderr = new String(cmdResult.getStderr());
			}
			MetadataSource metadataSource = new MetadataSource(
					cmdResult.getExitCode(), stdout, stderr, source);
			int exitCode = cmdResult.getExitCode();
			String exitCodeString = "";
			switch (exitCode) {
			case 0:
				exitCodeString = "ok";
				break;
			case -1:
				exitCodeString = "failed (-1)";
				break;
			default:
				exitCodeString = String.format("error (%s)", exitCode);
				break;
			}
			result.add(new ImageMetadata("exit-code", exitCodeString,
					metadataSource));
			if (cmdResult.getExitCode() == 0) {
				Map<String, MetadataSource> sources = new HashMap<String, MetadataSource>();
				List<Entry> entries = transformer.transform(
						cmdResult.getStdout(), cmdResult.getStderr());
				String version = null;
				for (Entry entry : entries) {
					if (entry.getKey().trim().toLowerCase().equals(VERSION_PROPERTY)) {						
						version = entry.getValue();
						metadataSource.setVersion(version);
						logger.info("Tool {} has version {}", source, version);
					}
				}
				for (Entry entry : entries) {
					ImageMetadata metadata = null;
					if (entry.getSource() == null) {
						metadata = new ImageMetadata(entry.getKey(),
								entry.getValue(), metadataSource);
					} else {
						MetadataSource newSource = sources.get(entry
								.getSource());
						if (newSource == null) {
							newSource = new MetadataSource(
									cmdResult.getExitCode(), stdout, stderr,
									entry.getSource());
							newSource.setVersion(version);
							sources.put(entry.getSource(), newSource);
						}
						metadata = new ImageMetadata(entry.getKey(),
								entry.getValue(), newSource);
					}
					if (units != null) {
						String unit = units.get(entry.getKey());
						metadata.setUnit(unit);
					}
					result.add(metadata);
				}
			}
		} catch (Exception ex) {
			logger.error(
					"Exception thrown when executing external metadata extractor",
					ex);
		}
		return result;
	}
}
