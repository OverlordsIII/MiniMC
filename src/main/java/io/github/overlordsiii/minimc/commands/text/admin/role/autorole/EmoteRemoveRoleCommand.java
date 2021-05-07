package io.github.overlordsiii.minimc.commands.text.admin.role.autorole;

import java.awt.Color;

import io.github.overlordsiii.minimc.Start;
import io.github.overlordsiii.minimc.api.EmbedCreator;
import io.github.overlordsiii.minimc.api.command.BaseCommand;
import io.github.overlordsiii.minimc.config.JsonHandler;
import io.github.overlordsiii.minimc.config.PropertiesHandler;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;

public class EmoteRemoveRoleCommand implements BaseCommand<GuildMessageReactionRemoveEvent> {

	@Override
	public void execute(GuildMessageReactionRemoveEvent event) {

		JsonHandler jsonHandler = Start.GUILD_MANAGER.getEmoteConfig().get(event.getGuild());

		long msgId = jsonHandler.getObj().get("messageId").getAsLong();

		if (event.getMember() == null || event.getUser() == null) {
			return;
		}

		PropertiesHandler handler = Start.GUILD_MANAGER.getGuildProperties().get(event.getGuild());

		event.retrieveMessage().queue(message -> {
			if (message.getIdLong() == msgId) {
				if (event.getReactionEmote().isEmote()) {
					Emote emote = event.getReactionEmote().getEmote();

					if (jsonHandler.getObj().has(Long.toString(emote.getIdLong()))) {
						long roleId = jsonHandler.getObj().get(Long.toString(emote.getIdLong())).getAsLong();

						Role role = event.getGuild().getRoleById(roleId);

						if (role != null) {
							event.getGuild().removeRoleFromMember(event.getMember(), role).queue();

							MessageEmbed embed = new EmbedCreator()
								.setUser(event.getUser())
								.setTitle("Removed Role")
								.setColor(Color.BLUE)
								.appendDescription("Removed role " + role.getName() + " from " + event.getUser().getAsMention())
								.create(event.getUser());

							event.getUser().openPrivateChannel().queue(privateChannel -> {
								privateChannel.sendMessage(embed).queue();
							});


							TextChannel textChannel = event.getGuild().getTextChannelById(handler.getConfigOption("botLog"));

							assert textChannel != null;
							textChannel.sendMessage(embed).queue();
						}
					}
				}
			}
		});
	}
}
