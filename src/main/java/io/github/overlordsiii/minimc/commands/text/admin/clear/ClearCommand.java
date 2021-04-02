package io.github.overlordsiii.minimc.commands.text.admin.clear;

import com.google.common.base.Throwables;
import io.github.overlordsiii.minimc.api.command.TextCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class ClearCommand implements TextCommand {
	@Override
	public void execute(MessageReceivedEvent event) {
		String[] content = event.getMessage().getContentRaw().split("\\s+");
		int number;
		try {
			number = Integer.parseInt(content[1]);
		} catch (Exception e) {
			event.getChannel().sendMessage("Could not delete messages because " + content[1] + " is not a number!").queue();
			event.getChannel().sendMessage("Debug: " + e).queue();
			return;
		}

		if (number > 100) {
			event.getChannel().sendMessage("Cannot delete more than 100 messages at once!").queue();
			return;
		}

		event.getChannel().getIterableHistory()
			.takeAsync(number)
			.thenAccept(list -> event.getChannel().purgeMessages(list));

		event.getChannel().sendMessage("Deleted " + number + " messages").queue();
	}

	@Override
	public String getName() {
		return "clear";
	}

	@Override
	public Permission[] getNeededPermissions() {
		return new Permission[]{Permission.MESSAGE_MANAGE};
	}
}
