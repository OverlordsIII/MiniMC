package io.github.overlordsiii.minimc.api;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.StreamSupport;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.overlordsiii.minimc.api.hypix.Player;
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
		.put("verifyChannel", "")
		.put("verifyRole", "")
		.build();

	private final PropertiesHandler guildProperties;

	private final JsonHandler mutedConfig;

	private final JsonHandler emoteConfig;

	private final JsonHandler playerConfig;

	private final JsonHandler amongusConfig;

	private final List<Player> players = new ArrayList<>();

	public GuildExtension(Guild guild) throws IOException {

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

		playerConfig = new JsonHandler(path.resolve("players.json")).initialize();

		amongusConfig = new JsonHandler(path.resolve("amongus.json")).initialize();

		checkPlayers(playerConfig.getObj());

		System.out.println("poolers");
	}

	private void checkPlayers(JsonObject object) {

		Set<JsonElement> elements = getObjects(object);

		elements
			.stream()
			.map(JsonElement::getAsJsonObject)
			.map(Player::deserialize)
			.forEach(players::add);


	}

	private static Set<JsonElement> getObjects(JsonObject object) {
		Set<JsonElement> elements = new HashSet<>();

		object.entrySet().forEach(stringJsonElementEntry -> elements.add(stringJsonElementEntry.getValue()));

		return elements;
	}

	public JsonHandler getAmongusConfig() {
		return amongusConfig;
	}

	public List<Player> getPlayers() {
		return players;
	}

	public void addPlayer(Player player) {
		players.add(player);
	}

	public void removePlayer(Player player) {
		players.remove(player);
	}

	public JsonHandler getEmoteConfig() {
		return emoteConfig;
	}

	public JsonHandler getMutedConfig() {
		return mutedConfig;
	}

	public JsonHandler getPlayerConfig() {
		return playerConfig;
	}

	public PropertiesHandler getGuildProperties() {
		return guildProperties;
	}
}
