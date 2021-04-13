package io.github.overlordsiii.minimc.api.command;

import javax.annotation.Nonnull;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public interface TextCommand extends BaseCommand<MessageReceivedEvent> {

	@Nonnull String getName();

	default char getPrefix() {
		return '!';
	}
}
