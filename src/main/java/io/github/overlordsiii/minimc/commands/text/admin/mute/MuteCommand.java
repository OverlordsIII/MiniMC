package io.github.overlordsiii.minimc.commands.text.admin.mute;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import com.google.gson.JsonObject;
import io.github.overlordsiii.minimc.Main;
import io.github.overlordsiii.minimc.api.MutedEntry;
import io.github.overlordsiii.minimc.api.command.TextCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.IMentionable;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class MuteCommand implements TextCommand {
	@Override
	public String getName() {
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
			event.getChannel().sendMessage("Can't mute this bot!").queue();
			return;
		}

		if (mentionedUsers.isEmpty()) {
			event.getChannel().sendMessage("Can't mute anyone because you did not mention anyone in your message!").queue();
			return;
		}

		mutedRole.forEach(role -> mentionedUsers.forEach(member -> guild.addRoleToMember(member, role).queue()));



		String mentions = mentionedUsers.stream()
			.map(IMentionable::getAsMention)
			.collect(Collectors.joining(", "));

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
			}
		});

		message.getChannel().sendMessage("Muted " + mentions + " because \"" + reason +  "\"").queue();
	}

	@Override
	public Permission[] getNeededPermissions() {
		return new Permission[]{Permission.MESSAGE_MANAGE};
	}
}
