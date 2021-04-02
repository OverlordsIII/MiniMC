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
			event.getChannel().sendMessage("Can't unmute this bot!").queue();
			return;
		}

		if (mentionedUsers.isEmpty()) {
			event.getChannel().sendMessage("Can't unmute anyone because you did not mention anyone in your message!").queue();
			return;
		}

		mutedRole.forEach(role -> mentionedUsers.forEach(member -> guild.removeRoleFromMember(member, role).queue()));

		String mentions = mentionedUsers.stream()
			.map(IMentionable::getAsMention)
			.collect(Collectors.joining(", "));

		mentionedUsers.forEach(member -> {
			String id = Long.toString(member.getIdLong());

			if (Main.MUTED_CONFIG.getObj().has(id)) {
				Main.MUTED_CONFIG.getObj().remove(id);
			} else {
				event.getChannel().sendMessage("Could not unmute " + member.getAsMention() + " because they were not muted!").queue();
			}
		});

		try {
			Main.MUTED_CONFIG.save();
		} catch (IOException e) {
			e.printStackTrace();
		}

		event.getChannel().sendMessage("Unmuted " + mentions).queue();
	}

	@Override
	public String getName() {
		return "unmute";
	}

	@Override
	public Permission[] getNeededPermissions() {
		return new Permission[]{Permission.MESSAGE_MANAGE};
	}
}
