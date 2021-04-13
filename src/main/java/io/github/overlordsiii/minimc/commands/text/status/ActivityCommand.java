package io.github.overlordsiii.minimc.commands.text.status;

import java.io.IOException;

import io.github.overlordsiii.minimc.Main;
import io.github.overlordsiii.minimc.api.command.TextCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.managers.Presence;
import org.jetbrains.annotations.NotNull;

public class ActivityCommand implements TextCommand {
	@Override
	public @NotNull String getName() {
		return "activity";
	}

	@Override
	public void execute(MessageReceivedEvent event) {
		String command = getPrefix() + getName();

		String status = event.getMessage().getContentRaw().trim().substring(command.length()).trim();

		Presence presence = Main.JDA.getPresence();

		presence.setActivity(Activity.of(Main.CONFIG.getConfigOption("activityType", Activity.ActivityType::valueOf), status));

		event.getChannel().sendMessage("Set activity to \"" + status + "\"").queue();

		Main.CONFIG.setConfigOption("activity", status);

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
