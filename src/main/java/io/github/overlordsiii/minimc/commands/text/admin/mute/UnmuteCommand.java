package io.github.overlordsiii.minimc.commands.text.admin.mute;

import java.awt.Color;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import io.github.overlordsiii.minimc.Start;
import io.github.overlordsiii.minimc.api.EmbedCreator;
import io.github.overlordsiii.minimc.api.command.TextCommand;
import io.github.overlordsiii.minimc.config.JsonHandler;
import io.github.overlordsiii.minimc.config.PropertiesHandler;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

public class UnmuteCommand implements TextCommand {
	@Override
	public void execute(MessageReceivedEvent event) {
		Message message = event.getMessage();

		Guild guild = event.getGuild();

		PropertiesHandler handler = Start.GUILD_MANAGER.getGuildProperties().get(event.getGuild());

		JsonHandler jsonHandler = Start.GUILD_MANAGER.getMutedGuildConfig().get(event.getGuild());

		String content = message.getContentDisplay().toLowerCase(Locale.ROOT);

		List<Role> mutedRole = guild.getRolesByName(handler.getConfigOption("mutedRole"), true);

		List<Member> mentionedUsers = message.getMentionedMembers();

		boolean containsMiniMC = mentionedUsers.stream()
			.map(Member::getUser)
			.collect(Collectors.toList())
			.contains(Start.JDA.getSelfUser());

		if (containsMiniMC) {

			MessageEmbed embed = new EmbedCreator()
				.setTitle("Can't unmute this bot!")
				.setColor(Color.RED)
				.setUser(event.getAuthor())
				.create(event.getAuthor());

			event.getChannel().sendMessage(embed).queue();
			return;
		}

		if (mentionedUsers.isEmpty()) {

			MessageEmbed embed = new EmbedCreator()
				.addErrorEmbed()
				.addField("Cannot unmute anyone!", "You didn't mention anyone to unmute in your message!")
				.setUser(event.getAuthor())
				.create(event.getAuthor());

			event.getChannel().sendMessage(embed).queue();
			return;
		}

		for (Role role : mutedRole) {
			for (Member mentionedUser : mentionedUsers) {
				if (!guild.getMembersWithRoles(role).contains(mentionedUser)) {
					throw new IllegalStateException("Could not unmute " + mentionedUser.getAsMention() + "because they did not have " + role.getAsMention() + " role");
				}
			}
		}

		mutedRole.forEach(role -> mentionedUsers.forEach(member -> {
			guild.removeRoleFromMember(member, role).queue();
		}));

		mentionedUsers.forEach(member -> {
			String id = Long.toString(member.getIdLong());

			if (jsonHandler.getObj().has(id)) {
				jsonHandler.getObj().remove(id);

				MessageEmbed embed = new EmbedCreator()
					.setColor(Color.CYAN)
					.setUser(member.getUser())
					.setTitle("Unmuted User")
					.mentionUser("User", member.getUser())
					.create(event.getAuthor());

				event.getChannel().sendMessage(embed).queue();
			} else {

				MessageEmbed embed = new EmbedCreator()
					.setUser(member.getUser())
					.setColor(Color.RED)
					.setTitle("Could not unmute!")
					.mentionUser("Could not unmute User!", member.getUser())
					.addField("Reason", "because they were not muted in the first place")
					.create(event.getAuthor());

				event.getChannel().sendMessage(embed).queue();
			}


		});

		try {
			jsonHandler.save();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public @NotNull String getName() {
		return "unmute";
	}

	@Override
	public Permission[] getNeededPermissions() {
		return new Permission[]{Permission.MESSAGE_MANAGE};
	}
}
