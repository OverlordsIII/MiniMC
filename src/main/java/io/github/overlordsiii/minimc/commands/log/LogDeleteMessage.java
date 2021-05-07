package io.github.overlordsiii.minimc.commands.log;

import java.awt.Color;
import java.util.Date;
import java.util.List;
import java.util.Map;

import io.github.overlordsiii.minimc.Start;
import io.github.overlordsiii.minimc.api.EmbedCreator;
import io.github.overlordsiii.minimc.api.command.BaseCommand;
import io.github.overlordsiii.minimc.config.PropertiesHandler;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;

public class LogDeleteMessage implements BaseCommand<MessageDeleteEvent> {
	@Override
	public void execute(MessageDeleteEvent event) {

		String id = event.getMessageId();

		PropertiesHandler handler = Start.GUILD_MANAGER.getGuildProperties().get(event.getGuild());

		TextChannel channel = event.getGuild().getTextChannelById(handler.getConfigOption("botLog"));

		Map<String, Message> messageMap = Start.COMMAND_HANDLER.getSentMessages();

		if (messageMap.containsKey(id)) {
			Message message = messageMap.get(id);

			MessageEmbed messageEmbed = new EmbedCreator()
				.setUser(message.getAuthor())
				.setColor(Color.CYAN)
				.setTitle("A message was deleted!")
				.mentionUser("Who", message.getAuthor()) //we are assuming here that the author of the message deleted it. Of course, we cannot find out who actually deleted the message, only the author
				.addField("Where", "<#" + message.getChannel().getIdLong() + ">")
				.addField("Content", message.getContentRaw())
				.addField("When was it deleted", new Date().toString())
				.create();

			assert channel != null;
			channel.sendMessage(messageEmbed).queue();
		}

	}
}
