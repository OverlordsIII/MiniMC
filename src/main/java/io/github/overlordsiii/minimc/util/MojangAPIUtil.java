package io.github.overlordsiii.minimc.util;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.io.IOUtils;

public class MojangAPIUtil {
	public static String ignToUuid(String ign) {
		String link = "https://api.mojang.com/users/profiles/minecraft/" + ign;

		String json;

		try {
			json = IOUtils.toString(new URL(link), StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		JsonObject object = JsonParser.parseString(json).getAsJsonObject();

		return object.get("id").getAsString();
	}
}
