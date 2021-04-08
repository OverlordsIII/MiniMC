package io.github.overlordsiii.minimc.commands.text.admin.clear;

import java.awt.Color;

import com.google.common.base.Throwables;
import io.github.overlordsiii.minimc.api.EmbedCreator;
import io.github.overlordsiii.minimc.api.command.TextCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class ClearCommand implements TextCommand {
	@Override
	public void execute(MessageReceivedEvent event) {
		String[] content = event.getMessage().getContentRaw().split("\\s+");
		int number;
		try {
			number = Integer.parseInt(content[1]);
		} catch (NumberFormatException e) {

			MessageEmbed embed = new EmbedCreator()
				.addErrorEmbed()
				.addField("Could not delete messages!", "Reason: " + content[1] + " is either not a number or is too large!")
				.create(event.getAuthor());


			event.getChannel().sendMessage(embed).queue();
			return;
		}

		if (number > 100) {

			MessageEmbed embed = new EmbedCreator()
				.addErrorEmbed()
				.addField("Cannot delete messages!", "Reason: Cannot delete more than 100 messages at once!")
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
			.takeAsync(number)
			.thenAccept(list -> event.getChannel().purgeMessages(list))
			.thenRun(() -> event.getChannel().sendMessage(embed).queue());
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
