package io.github.overlordsiii.minimc.commands.text.admin.clear;

import java.awt.Color;
import java.util.stream.Collectors;

import io.github.overlordsiii.minimc.api.EmbedCreator;
import io.github.overlordsiii.minimc.api.command.TextCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

public class ClearUserCommand implements TextCommand {
	@Override
	public void execute(MessageReceivedEvent event) {


		Message message = event.getMessage();

		TextChannel channel = message.getTextChannel();

		if (message.getMentionedMembers().isEmpty()) {
			throw new IllegalArgumentException("You need to mention someone to clear messages from them");
		}
		message.getMentionedMembers().forEach(member -> {
			channel.getHistory().retrievePast(100)
				.queue(messages -> {

					messages = messages.stream()
						.filter(message1 -> message1.getAuthor().equals(member.getUser()))
						.collect(Collectors.toList());
					channel.deleteMessages(messages).queue();
					channel.sendMessage(getClearUserEmbed(messages.size(), member, event.getAuthor())).queue();
				});
		});


	}

	private static MessageEmbed getClearUserEmbed(int numbers, Member deletingFrom, User author) {
		return new EmbedCreator()
			.setUser(deletingFrom.getUser())
			.setColor(Color.GREEN)
			.setTitle("Deleted Messages")
			.appendDescription( "Deleted " + numbers + " messages from " + deletingFrom.getAsMention())
			.create(author);
	}

	@Override
	public @NotNull String getName() {
		return "clearUser";
	}

	@Override
	public Permission[] getNeededPermissions() {
		return new Permission[]{Permission.MESSAGE_MANAGE};
	}
}
