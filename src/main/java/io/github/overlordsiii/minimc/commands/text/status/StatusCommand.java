package io.github.overlordsiii.minimc.commands.text.status;

import java.io.IOException;
import java.util.Locale;

import io.github.overlordsiii.minimc.Main;
import io.github.overlordsiii.minimc.api.command.TextCommand;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.managers.Presence;

public class StatusCommand implements TextCommand {
	@Override
	public String getName() {
		return "status";
	}

	@Override
	public void execute(MessageReceivedEvent event) {
		String command = getPrefix() + getName();

		String status = event.getMessage().getContentRaw().trim().substring(command.length()).toLowerCase(Locale.ROOT).trim();

		Presence presence = Main.JDA.getPresence();
		if (status.contains("online") || status.contains(":online:")) {
			presence.setStatus(OnlineStatus.ONLINE);
			event.getChannel().sendMessage("Set status to online").queue();
		} else if (status.contains("idle") || status.contains(":idle:")) {
			presence.setStatus(OnlineStatus.IDLE);
			event.getChannel().sendMessage("Set status to idle").queue();
		} else if (status.contains("dnd") || status.contains("do not disturb") || status.contains(":dnd:")) {
			presence.setStatus(OnlineStatus.DO_NOT_DISTURB);
			event.getChannel().sendMessage("Set status to dnd").queue();
		} else if (status.contains("invis") || status.contains("invisible") || status.contains(":invis:")) {
			presence.setStatus(OnlineStatus.INVISIBLE);
			event.getChannel().sendMessage("Set status to invis").queue();
		} else {
			event.getChannel().sendMessage("Could not find the status you were looking for!").queue();
		}

		Main.CONFIG.setConfigOption("status", presence.getStatus().toString());

		try {
			Main.CONFIG.save();
		} catch (IOException e) {
			throw new RuntimeException("Could not save config!", e);
		}
	}

	@Override
	public Permission[] getNeededPermissions() {
		return new Permission[]{Permission.NICKNAME_MANAGE};
	}

}
