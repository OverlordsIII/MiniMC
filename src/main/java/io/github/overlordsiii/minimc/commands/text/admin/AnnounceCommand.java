package io.github.overlordsiii.minimc.commands.text.admin;

import java.awt.Color;

import io.github.overlordsiii.minimc.Start;
import io.github.overlordsiii.minimc.api.EmbedCreator;
import io.github.overlordsiii.minimc.api.command.TextCommand;
import io.github.overlordsiii.minimc.config.PropertiesHandler;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.internal.utils.Checks;
import org.jetbrains.annotations.NotNull;

public class AnnounceCommand implements TextCommand {
	@Override
	public void execute(MessageReceivedEvent event) {

		Message message = event.getMessage();

		int index = message.getContentRaw().indexOf(" ");

		PropertiesHandler handler = Start.GUILD_MANAGER.getExtension(event.getGuild()).getGuildProperties();

		Checks.check(index != -1, "Must have space when executing command !announce");

		String messageContent = message.getContentRaw().substring(index);

		TextChannel storeChannel = event.getGuild().getTextChannelById(handler.getConfigOption("announcementChannel"));

		MessageEmbed embed = new EmbedCreator()
			.setUser(event.getAuthor())
			.setTitle("Admin Message!")
			.appendDescription(messageContent)
			.create(event.getAuthor());
			storeChannel.sendMessage(embed).queue();


		MessageEmbed otherEmbed = new EmbedCreator()
			.setColor(Color.orange)
			.setUser(event.getAuthor())
			.setTitle("Announced Message!")
			.addField("Content", messageContent)
			.create(event.getAuthor());

		event.getChannel()
			.sendMessage(otherEmbed)
			.queue();



	}

	@NotNull
	@Override
	public String getName() {
		return "announce";
	}
}
