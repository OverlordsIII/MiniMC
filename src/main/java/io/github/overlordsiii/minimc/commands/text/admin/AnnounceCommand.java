package io.github.overlordsiii.minimc.commands.text.admin;

import io.github.overlordsiii.minimc.Main;
import io.github.overlordsiii.minimc.api.EmbedCreator;
import io.github.overlordsiii.minimc.api.command.TextCommand;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.internal.utils.Checks;
import org.jetbrains.annotations.NotNull;

public class AnnounceCommand implements TextCommand {
	@Override
	public void execute(MessageReceivedEvent event) {

		Message message = event.getMessage();

		int index = message.getContentRaw().indexOf(" ");

		Checks.check(index != -1, "Must have space when executing command !announce");

		String messageContent = message.getContentRaw().substring(index);



		event.getGuild().getTextChannelsByName(Main.CONFIG.getConfigOption("announcmentChannel"), true).forEach(storeChannel -> {
			MessageEmbed embed = new EmbedCreator()
				.setUser(event.getAuthor())
				.setTitle("Admin Message!")
				.appendDescription(messageContent)
				.create(event.getAuthor());

			storeChannel.sendMessage(embed).queue();
		});



	}

	@NotNull
	@Override
	public String getName() {
		return "announce";
	}
}
