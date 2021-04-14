package io.github.overlordsiii.minimc.commands.log;

import java.awt.Color;
import java.util.List;
import java.util.Map;

import io.github.overlordsiii.minimc.Main;
import io.github.overlordsiii.minimc.api.EmbedCreator;
import io.github.overlordsiii.minimc.api.command.BaseCommand;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;

public class LogDeleteMessage implements BaseCommand<MessageDeleteEvent> {
	@Override
	public void execute(MessageDeleteEvent event) {

		String id = event.getMessageId();

		List<TextChannel> channels = event.getGuild().getTextChannelsByName(Main.CONFIG.getConfigOption("botLog"), true);

        if (channels.isEmpty()) return; // no log channel
        TextChannel channel = channels.get(0); // get first match

		Map<String, Message> messageMap = Main.COMMAND_HANDLER.getSentMessages();

		if (messageMap.containsKey(id)) {
			Message message = messageMap.get(id);

			MessageEmbed messageEmbed = new EmbedCreator()
				.setUser(message.getAuthor())
				.setColor(Color.CYAN)
				.setTitle("A message was deleted!")
				.mentionUser("Who", message.getAuthor()) //we are assuming here that the author of the message deleted it. Of course, we cannot find out who actually deleted the message, only the author
				.addField("Where", "<#" + message.getChannel().getIdLong() + ">")
				.addField("Content", message.getContentRaw())
				.create();

			channel.sendMessage(messageEmbed).queue();
		}

	}
}
