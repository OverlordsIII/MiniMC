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
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

public class MuteCommand implements TextCommand {
	@Override
	public @NotNull String getName() {
		return "mute";
	}

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
				.setTitle("Can't mute this bot!")
				.setColor(Color.RED)
				.setUser(event.getAuthor())
				.create(event.getAuthor());

			event.getChannel().sendMessage(embed).queue();
			return;
		}

		if (mentionedUsers.isEmpty()) {
			MessageEmbed embed = new EmbedCreator()
				.addErrorEmbed()
				.addField("Cannot mute anyone!", "You didn't mention anyone to mute in your message!")
				.setUser(event.getAuthor())
				.create(event.getAuthor());


			event.getChannel().sendMessage(embed).queue();
			return;
		}

		mutedRole.forEach(role -> mentionedUsers.forEach(member -> guild.addRoleToMember(member, role).queue()));


		String reason = "None";

		if (content.contains("reason: ")) {
			reason = content.substring(content.indexOf("reason: ") + "reason: ".length()).trim();
		}

		String finalReason = reason;

		mentionedUsers.forEach(member -> {
			MutedEntry entry = new MutedEntry(member.getUser().getIdLong(), finalReason, event.getAuthor().getIdLong());

			JsonObject object = entry.serialize().getAsJsonObject();
			Main.MUTED_CONFIG.getObj().add(member.getId(), object);

			try {
				Main.MUTED_CONFIG.save();
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}

			MessageEmbed embed = new EmbedCreator()
				.setColor(Color.CYAN)
				.setUser(member.getUser())
				.setTitle("Muted User")
				.mentionUser("User", member.getUser())
				.addField("Reason", finalReason)
				.create(event.getAuthor());


			message.getChannel().sendMessage(embed).queue();
		});


	}

	@Override
	public Permission[] getNeededPermissions() {
		return new Permission[]{Permission.MESSAGE_MANAGE};
	}
}
