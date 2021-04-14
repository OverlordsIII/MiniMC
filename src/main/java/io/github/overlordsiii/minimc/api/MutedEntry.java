package io.github.overlordsiii.minimc.api;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class MutedEntry implements JsonSerializer<MutedEntry>, JsonDeserializer<MutedEntry> {

	private final long mutedUser;

	private final String reason;

	private final long moderator;

	public MutedEntry(long mutedUser, String reason, long moderator) {
		this.mutedUser = mutedUser;
		this.reason = reason;
		this.moderator = moderator;
	}

	public String getReason() {
		return reason;
	}

	public long getModerator() {
		return moderator;
	}

	public long getMutedUser() {
		return mutedUser;
	}

	@Override
	public MutedEntry deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		return deserialize(json);
	}


	@Override
	public JsonElement serialize(MutedEntry src, Type typeOfSrc, JsonSerializationContext context) {
		JsonObject object = new JsonObject();

		object.addProperty("reason", src.getReason());
		object.addProperty("mutedUser", src.getMutedUser());
		object.addProperty("moderator", src.getModerator());

		return object;
	}

	public JsonElement serialize() {
		return serialize(this, this.getClass(), null);
	}

	public static MutedEntry deserialize(JsonElement element) {
		JsonObject object = element.getAsJsonObject();

		return new MutedEntry(object.get("mutedUser").getAsLong(), object.get("reason").getAsString(), object.get("moderator").getAsLong());
	}


}
