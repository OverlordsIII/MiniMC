package io.github.overlordsiii.minimc.api;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.common.collect.ImmutableMap;
import io.github.overlordsiii.minimc.config.JsonHandler;
import io.github.overlordsiii.minimc.config.PropertiesHandler;
import net.dv8tion.jda.api.entities.Guild;

public class GuildExtension {

	private static final Path PARENT_PATH = Paths.get("src", "main", "resources");

	private static final ImmutableMap<String, String> configKeys = ImmutableMap.<String, String>builder()
		.put("mutedRole", "")
		.put("defaultRole", "")
		.put("botLog", "")
		.put("roleChannel", "")
		.put("announcementChannel", "")
		.put("welcomeChannel", "")
		.put("spamChannel", "")
		.put("imposters", "1")
		.build();

	private final PropertiesHandler guildProperties;

	private final JsonHandler mutedConfig;

	private final JsonHandler emoteConfig;

	private final JsonHandler uuidConfig;

	private final Guild guild;

	public GuildExtension(Guild guild) throws IOException {
		this.guild = guild;

		Path path = PARENT_PATH.resolve(Long.toString(guild.getIdLong()));

		if (!Files.exists(path)) {
			Files.createDirectories(path);
		}

		mutedConfig = new JsonHandler(path.resolve("mutedMembers.json")).initialize();

		emoteConfig = new JsonHandler(path.resolve("emoteToRole.json")).initialize();

		PropertiesHandler.Builder builder = PropertiesHandler.builder();

		builder.setComment("Sets the config options for the guild " + guild.getName());
		builder.setPath(path.resolve("guild.properties"));

		configKeys.forEach(builder::addConfigOption);

		guildProperties = builder.buildPath();

		uuidConfig = new JsonHandler(path.resolve("idToUuid.json")).initialize();
	}

	public JsonHandler getEmoteConfig() {
		return emoteConfig;
	}

	public JsonHandler getMutedConfig() {
		return mutedConfig;
	}

	public JsonHandler getUuidConfig() {
		return uuidConfig;
	}

	public PropertiesHandler getGuildProperties() {
		return guildProperties;
	}
}
