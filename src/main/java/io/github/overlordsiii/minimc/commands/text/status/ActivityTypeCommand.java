package io.github.overlordsiii.minimc.commands.text.status;

import java.io.IOException;
import java.util.Locale;
import java.util.Objects;

import io.github.overlordsiii.minimc.Start;
import io.github.overlordsiii.minimc.api.command.TextCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.managers.Presence;
import org.jetbrains.annotations.NotNull;

public class ActivityTypeCommand implements TextCommand {
	@Override
	public @NotNull String getName() {
		return "activityType";
	}

	@Override
	public void execute(MessageReceivedEvent event) {
		String command = getPrefix() + getName();

		String status = event.getMessage().getContentRaw().trim().substring(command.length()).toLowerCase(Locale.ROOT).trim();

		Presence presence = Start.JDA.getPresence();

		String currentStatus = Start.CONFIG.getConfigOption("status");

		if (status.contains("playing") || status.contains("default")) {
			presence.setActivity(Activity.of(Activity.ActivityType.DEFAULT, currentStatus));
		} else if (status.contains("listening")) {
			presence.setActivity(Activity.of(Activity.ActivityType.LISTENING, currentStatus));
		} else if (status.contains("watching")) {
			presence.setActivity(Activity.of(Activity.ActivityType.WATCHING, currentStatus));
		} else if (status.contains("competing")) {
			presence.setActivity(Activity.of(Activity.ActivityType.COMPETING, currentStatus));
		}

		event.getChannel().sendMessage("Set activity type to \"" + Objects.requireNonNull(presence.getActivity()).getType().toString() + "\"").queue();

		Start.CONFIG.setConfigOption("activityType", Objects.requireNonNull(presence.getActivity()).getType().toString());

		try {
			Start.CONFIG.save();
		} catch (IOException e) {
			throw new RuntimeException("Could not save config!", e);
		}
	}

	@Override
	public Permission[] getNeededPermissions() {
		return new Permission[]{Permission.NICKNAME_MANAGE};
	}
}
