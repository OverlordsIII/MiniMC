package io.github.overlordsiii.minimc.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import net.dv8tion.jda.api.entities.Guild;

public class GuildManager {

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

	private final Map<Guild, PropertiesHandler> guildProperties = new HashMap<>();

	private final Map<Guild, JsonHandler> mutedGuildConfig = new HashMap<>();

	private final Map<Guild, JsonHandler> emoteConfig = new HashMap<>();

	private final Map<Guild, JsonHandler> msgConfig = new HashMap<>();



	public void addGuild(Guild guild) {
		try {
			addGuildProperties(guild);
			addMutedConfig(guild);
			addEmoteConfig(guild);
			//addMessageConfig(guild);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}



	private void addEmoteConfig(Guild guild) throws IOException {
		if (!emoteConfig.containsKey(guild)) {
			Path path = PARENT_PATH.resolve(Long.toString(guild.getIdLong()));

			if (!Files.exists(path)) {
				Files.createDirectories(path);
			}

			emoteConfig.put(guild, new JsonHandler(path.resolve("emoteToRole.json")).initialize());
		}
	}

	private void addGuildProperties(Guild guild) throws IOException {
		if (!guildProperties.containsKey(guild)) {
			PropertiesHandler.Builder builder = PropertiesHandler.builder();

			Path path = PARENT_PATH.resolve(Long.toString(guild.getIdLong()));

			if (!Files.exists(path)) {
				Files.createDirectories(path);
			}

			builder.setComment("Sets the config options for the guild " + guild.getName());
			builder.setPath(path.resolve("guild.properties"));

			configKeys.forEach(builder::addConfigOption);

			guildProperties.put(guild, builder.buildPath());
		}
	}

	private void addMutedConfig(Guild guild) throws IOException {
		if (!mutedGuildConfig.containsKey(guild)) {
			Path path = PARENT_PATH.resolve(Long.toString(guild.getIdLong()));

			if (!Files.exists(path)) {
				Files.createDirectories(path);
			}

			mutedGuildConfig.put(guild, new JsonHandler(path.resolve("mutedMembers.json")).initialize());
		}
	}

	private void addMessageConfig(Guild guild) throws IOException {
		if (!msgConfig.containsKey(guild)) {
			Path path = PARENT_PATH.resolve(Long.toString(guild.getIdLong()));

			if (!Files.exists(path)) {
				Files.createDirectories(path);
			}

			msgConfig.put(guild, new JsonHandler(path.resolve("messages.json")).initialize());
		}
	}

	public Map<Guild, JsonHandler> getEmoteConfig() {
		return ImmutableMap.copyOf(emoteConfig);
	}

	public Map<Guild, JsonHandler> getMutedGuildConfig() {
		return ImmutableMap.copyOf(mutedGuildConfig);
	}

	public Map<Guild, PropertiesHandler> getGuildProperties() {
		return ImmutableMap.copyOf(guildProperties);
	}

	public Map<Guild, JsonHandler> getMsgConfig() {
		return ImmutableMap.copyOf(msgConfig);
	}

	public void serialize(Map<?, JsonHandler> map) {
		map.forEach((guild, jsonHandler) -> {
			try {
				jsonHandler.save();
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

	public void serialize() {
		serialize(emoteConfig);
		serialize(mutedGuildConfig);
		serialize(msgConfig);
	}
}
