package io.github.overlordsiii.minimc.commands.text.admin.mute;

import java.awt.Color;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import com.google.gson.JsonObject;
import io.github.overlordsiii.minimc.Main;
import io.github.overlordsiii.minimc.api.EmbedCreator;
import io.github.overlordsiii.minimc.api.MutedEntry;
import io.github.overlordsiii.minimc.api.command.TextCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.IMentionable;
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

		String content = message.getContentDisplay().toLowerCase(Locale.ROOT);

		List<Role> mutedRole = guild.getRolesByName(Main.CONFIG.getConfigOption("mutedRole"), true);

		List<Member> mentionedUsers = message.getMentionedMembers();

		boolean containsMiniMC = mentionedUsers.stream()
			.map(Member::getUser)
			.collect(Collectors.toList())
			.contains(Main.JDA.getSelfUser());

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

		mutedRole.forEach(role -> mentionedUsers.forEach(member -> guild.removeRoleFromMember(member, role).queue()));

		mentionedUsers.forEach(member -> {
			String id = Long.toString(member.getIdLong());

			if (Main.MUTED_CONFIG.getObj().has(id)) {
				Main.MUTED_CONFIG.getObj().remove(id);

				MessageEmbed embed = new EmbedCreator()
					.setColor(Color.CYAN)
					.setUser(member.getUser())
					.setTitle("Unmuted User")
					.addField("", member.getAsMention())
					.create(event.getAuthor());

				event.getChannel().sendMessage(embed).queue();
			} else {

				MessageEmbed embed = new EmbedCreator()
					.setUser(member.getUser())
					.setColor(Color.RED)
					.setTitle("Could not unmute!")
					.addField("Could not unmute User!", member.getAsMention())
					.addField("Reason", "because they were not muted in the first place")
					.create(event.getAuthor());

				event.getChannel().sendMessage(embed).queue();
			}


		});

		try {
			Main.MUTED_CONFIG.save();
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
