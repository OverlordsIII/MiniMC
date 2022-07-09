package io.github.overlordsiii.minimc.api.hypix;

import java.util.UUID;

import com.google.gson.JsonObject;
import io.github.overlordsiii.minimc.util.MojangAPIUtil;
import io.github.overlordsiii.minimc.util.UUIDUtil;

public class Player {
	private final String name;

	private final String discordId;

	private final UUID uuid;

	private int eloRating = 0;

	public Player(String name, String discordId, UUID uuid) {
		this.name = name;
		this.discordId = discordId;
		this.uuid = uuid;
	}

	public int getEloRating() {
		return eloRating;
	}

	public String getDiscordId() {
		return discordId;
	}

	public String getName() {
		return name;
	}

	public UUID getUuid() {
		return uuid;
	}

	public Player(String name, String discordId) {
		this(name, discordId, UUIDUtil.stringToUuid(MojangAPIUtil.ignToUuid(name)));
	}

	public Player(UUID uuid, String discordId) {
		this(MojangAPIUtil.uuidToName(uuid.toString()), discordId, uuid);
	}

	public void setEloRating(int eloRating) {
		this.eloRating = eloRating;
	}

	public void incrementEloRating(int increment) {
		setEloRating(this.eloRating + increment);
	}

	public void decrementEloRating(int decrement) {
		int newRating = eloRating - decrement;

		if (newRating < 0) {
			newRating = 0;
		}

		setEloRating(newRating);
	}

	public JsonObject serialize() {
		JsonObject object = new JsonObject();

		object.addProperty("discordId", discordId);
		object.addProperty("uuid", String.valueOf(uuid));
		object.addProperty("eloRating", eloRating);

		return object;
	}

	public static Player deserialize(JsonObject object) {
		String discordId = object.get("discordId").getAsString();

		String uuid = object.get("uuid").getAsString();

		int eloRating = object.get("eloRating").getAsInt();

		Player player = new Player(UUIDUtil.stringToUuid(uuid), discordId);

		player.setEloRating(eloRating);

		return player;
	}





}
