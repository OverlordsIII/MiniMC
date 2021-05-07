package io.github.overlordsiii.minimc.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.function.Function;

public class PropertiesHandler {

	private final Path propertiesPath;

	private final Map<String, String> configValues;

	private final String comment;

	private PropertiesHandler(String filename, Map<String, String> configValues, String comment) {
		this(Paths.get("src", "main", "resources").resolve(filename), configValues, comment);
	}

	private PropertiesHandler(Path propertiesPath, Map<String, String> configValues, String comment) {
		this.propertiesPath = propertiesPath;
		this.comment = comment;
		this.configValues = configValues;
	}

	public void initialize() {
		try {
			load();
			save();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void load() throws IOException {

		if (!Files.exists(propertiesPath)) {
			return;
		}

		Properties properties = new Properties();

		properties.load(Files.newInputStream(propertiesPath));

		properties.forEach((o, o2) -> configValues.put(o.toString(), o2.toString()));

	}

	public void setConfigOption(String name, String value) {
		configValues.replace(name, value);
	}

	public void save() throws IOException {

		if (!Files.exists(propertiesPath.getParent())) {
			throw new RuntimeException("Could not find resources dir!");
		}

		Properties properties = new Properties();

		configValues.forEach(properties::put);

		properties.store(Files.newOutputStream(propertiesPath), comment);

	}

	public static Builder builder() {
		return new Builder();
	}

	public <T> T getConfigOption(String key, Function<String, T> parser) {
		return parser.apply(configValues.get(key));
	}

	public String getConfigOption(String key) {
		return getConfigOption(key, Function.identity());
	}

	public static class Builder {

		private final Map<String, String> configValues = new HashMap<>();
		private String filename;
		private String comment;
		private Path propertiesPath;

		private Builder() {}

		public Builder addConfigOption(String key, String defaultValue) {
			configValues.put(key, defaultValue);
			return this;
		}

		public Builder setFileName(String fileName) {
			this.filename = fileName;
			return this;
		}

		public Builder setComment(String comment) {
			this.comment = comment;

			return this;
		}

		public Builder setPath(Path propertiesPath) {
			this.propertiesPath = propertiesPath;

			return this;
		}

		public PropertiesHandler build() {

			Objects.requireNonNull(filename, "Must set filename before building PropertiesHandler!");
			Objects.requireNonNull(comment, "Must set comments needed before building PropertiesHandler!");

			PropertiesHandler propertiesHandler = new PropertiesHandler(filename, configValues, comment);
			propertiesHandler.initialize();
			return propertiesHandler;
		}

		public PropertiesHandler buildPath() {
			Objects.requireNonNull(comment, "Must set comments needed before building PropertiesHandler!");
			Objects.requireNonNull(propertiesPath, "Must set path before building PropertiesHandler!");

			PropertiesHandler propertiesHandle = new PropertiesHandler(propertiesPath, configValues, comment);

			propertiesHandle.initialize();

			return propertiesHandle;
		}
	}

}
