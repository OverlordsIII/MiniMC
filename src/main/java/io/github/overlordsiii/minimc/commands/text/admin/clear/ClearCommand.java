package io.github.overlordsiii.minimc.commands.text.admin.clear;

import java.awt.Color;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

import com.google.common.base.Throwables;
import io.github.overlordsiii.minimc.api.EmbedCreator;
import io.github.overlordsiii.minimc.api.command.TextCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

public class ClearCommand implements TextCommand {
	@Override
	public void execute(MessageReceivedEvent event) {
		String[] content = event.getMessage().getContentRaw().split("\\s+");
		int number;
		try {
			number = Integer.parseInt(content[1]);

			if (number < 0) {
				throw new NumberFormatException(number + " was negative");
			}
		} catch (NumberFormatException e) {

			MessageEmbed embed = new EmbedCreator()
				.addErrorEmbed()
				.addField("Could not delete messages!", "")
				.addField("Reason", content[1] + " is either not a number or is too large! See debug info below!")
				.addField("Debug Info","||" +  e.getLocalizedMessage() + "||")
				.create(event.getAuthor());

			e.printStackTrace();

			event.getChannel().sendMessage(embed).queue();
			return;
		}

		if (number > 100) {

			MessageEmbed embed = new EmbedCreator()
				.addErrorEmbed()
				.addField("Cannot delete messages!", "")
				.addField("Reason", "Cannot delete more than 100 messages at once!")
				.create(event.getAuthor());

			event.getChannel().sendMessage(embed).queue();
			return;
		}

		MessageEmbed embed = new EmbedCreator()
			.setColor(Color.GREEN)
			.setUser(event.getAuthor())
			.setTitle("Deleted " + number + " messages")
			.create(event.getAuthor());

		event.getChannel().getIterableHistory()
			.takeAsync(number + 1)
			.thenAccept(list -> event.getChannel().purgeMessages(list))
			.thenRun(() -> event.getChannel().sendMessage(embed).queue());
	}

	private static String getStackTrace(Throwable throwable) {
		StringWriter sw = new StringWriter();
		throwable.printStackTrace(new PrintWriter(sw));

		return sw.toString();
	}

	@Override
	public @NotNull String getName() {
		return "clear";
	}

	@Override
	public Permission[] getNeededPermissions() {
		return new Permission[]{Permission.MESSAGE_MANAGE};
	}
}
