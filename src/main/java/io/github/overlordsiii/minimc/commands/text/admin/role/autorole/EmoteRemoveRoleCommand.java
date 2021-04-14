package io.github.overlordsiii.minimc.commands.text.admin.role.autorole;

import java.awt.Color;

import io.github.overlordsiii.minimc.Main;
import io.github.overlordsiii.minimc.api.EmbedCreator;
import io.github.overlordsiii.minimc.api.command.BaseCommand;
import io.github.overlordsiii.minimc.api.command.TextCommand;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEmoteEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;
import org.jetbrains.annotations.NotNull;

public class EmoteRemoveRoleCommand implements BaseCommand<GuildMessageReactionRemoveEvent> {

	@Override
	public void execute(GuildMessageReactionRemoveEvent event) {

		long msgId = Main.EMOTE_TO_ROLE.getObj().get("messageId").getAsLong();

		if (event.getMember() == null || event.getUser() == null) {
			return;
		}

		event.retrieveMessage().queue(message -> {
			if (message.getIdLong() == msgId) {
				if (event.getReactionEmote().isEmote()) {
					Emote emote = event.getReactionEmote().getEmote();

					if (Main.EMOTE_TO_ROLE.getObj().has(Long.toString(emote.getIdLong()))) {
						long roleId = Main.EMOTE_TO_ROLE.getObj().get(Long.toString(emote.getIdLong())).getAsLong();

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

							event.getGuild().getTextChannelsByName(Main.CONFIG.getConfigOption("botLog"), true).forEach(textChannel -> textChannel.sendMessage(embed).queue());
						}
					}
				}
			}
		});
	}
}
