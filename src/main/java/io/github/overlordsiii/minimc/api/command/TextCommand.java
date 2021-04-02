package io.github.overlordsiii.minimc.api.command;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public interface TextCommand extends BaseCommand<MessageReceivedEvent> {

	String getName();

	default char getPrefix() {
		return '!';
	}
}
