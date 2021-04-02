package io.github.overlordsiii.minimc.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class JsonHandler {

	private JsonObject base = new JsonObject();
	private final Path propertiesPath;

	public static final Gson GSON = new GsonBuilder()
			.disableHtmlEscaping()
			.serializeNulls()
			.setPrettyPrinting()
			.create();

	public JsonHandler(String fileName) {
		this.propertiesPath = Paths.get("src", "main", "resources").resolve(fileName);
	}

	public JsonHandler initialize() {
		try {
			load();
			save();
		} catch (IOException e) {
			throw new RuntimeException("Could not load config files for json handler: " + propertiesPath.getFileName());
		}

		return this;
	}

	public void save() throws IOException {

		if (!Files.exists(propertiesPath.getParent())) {
			throw new RuntimeException("Could not find resources dir!");
		}

		String file = GSON.toJson(base);

		Files.writeString(propertiesPath, file);

	}

	public void load() throws IOException {

		if (!Files.exists(propertiesPath)) {
			return;
		}

		this.base = JsonParser.parseReader(Files.newBufferedReader(propertiesPath)).getAsJsonObject();
	}

	public JsonObject getObj() {
		return base;
	}
}
