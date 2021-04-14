package io.github.overlordsiii.minimc.commands.text.admin.role.autorole;

import java.awt.Color;

import io.github.overlordsiii.minimc.Main;
import io.github.overlordsiii.minimc.api.EmbedCreator;
import io.github.overlordsiii.minimc.api.command.BaseCommand;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;

public class EmoteAddedRoleCommand implements BaseCommand<GuildMessageReactionAddEvent> {
	@Override
	public void execute(GuildMessageReactionAddEvent event) {

		long msgId = Main.EMOTE_TO_ROLE.getObj().get("messageId").getAsLong();

		if (event.getUser().isBot()) {
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
							event.getGuild().addRoleToMember(event.getMember(), role).queue();

							MessageEmbed embed = new EmbedCreator()
								.setUser(event.getUser())
								.setTitle("Added Role")
								.setColor(Color.orange)
								.appendDescription("Added role " + role.getName() + " to " + event.getUser().getAsMention())
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
