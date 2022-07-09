package io.github.overlordsiii.minimc.config;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import io.github.overlordsiii.minimc.api.GuildExtension;
import net.dv8tion.jda.api.entities.Guild;

public class GuildManager {

	private final Map<Guild, PropertiesHandler> guildProperties = new HashMap<>();

	private final Map<Guild, GuildExtension> extensionMap = new HashMap<>();



	public void addGuild(Guild guild) {
		try {
			extensionMap.put(guild, new GuildExtension(guild));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public Map<Guild, PropertiesHandler> getGuildProperties() {
		return ImmutableMap.copyOf(guildProperties);
	}

	public GuildExtension getExtension(Guild guild) {
		return extensionMap.get(guild);
	}

}
