package io.github.overlordsiii.minimc.commands.hypix;

import java.awt.Color;
import java.io.IOException;

import com.google.gson.JsonObject;
import io.github.overlordsiii.minimc.Start;
import io.github.overlordsiii.minimc.api.EmbedCreator;
import io.github.overlordsiii.minimc.api.GuildExtension;
import io.github.overlordsiii.minimc.api.command.TextCommand;
import io.github.overlordsiii.minimc.api.hypix.Player;
import io.github.overlordsiii.minimc.config.JsonHandler;
import io.github.overlordsiii.minimc.config.PropertiesHandler;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

public class UnverifyCommand implements TextCommand {
	@Override
	public void execute(MessageReceivedEvent event) {

		PropertiesHandler propertiesHandler = Start.GUILD_MANAGER.getExtension(event.getGuild()).getGuildProperties();

		String id = propertiesHandler.getConfigOption("verifyChannel");

		String channelId = event.getChannel().getId();

		boolean bl = !channelId.equals(id);

		if (bl) {
			return;
		}



		GuildExtension guildExtension = Start.GUILD_MANAGER.getExtension(event.getGuild());

		JsonHandler handler = guildExtension.getPlayerConfig();

		JsonObject object = handler.getObj();

		if (!object.has(event.getAuthor().getId())) {
			throw new RuntimeException("You are not verified yet! Run !verify first");
		}

		JsonObject object1 = object.get(event.getAuthor().getId()).getAsJsonObject();

		guildExtension.removePlayer(Player.deserialize(object1));

		object.remove(event.getAuthor().getId());

		try {
			handler.save();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		Role role = propertiesHandler.getConfigOption("verifyRole",event.getGuild()::getRoleById);

		MessageEmbed embed = new EmbedCreator()
			.setTitle("Unverified User!")
			.setColor(Color.CYAN)
			.setUser(Start.JDA.getSelfUser())
			.appendDescription("Unverified " + event.getAuthor().getAsMention() + "!")
			.create(event.getAuthor());

		event.getGuild().removeRoleFromMember(event.getMember(), role).queue();

		event.getChannel().sendMessage(embed).queue();
	}

	@NotNull
	@Override
	public String getName() {
		return "unverify";
	}
}
