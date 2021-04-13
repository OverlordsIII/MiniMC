package io.github.overlordsiii.minimc.commands.text.admin.clear;

import java.awt.Color;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import io.github.overlordsiii.minimc.api.EmbedCreator;
import io.github.overlordsiii.minimc.api.command.TextCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

public class ClearUserCommand implements TextCommand {
	@Override
	public void execute(MessageReceivedEvent event) {


		Message message = event.getMessage();

		MessageChannel channel = event.getChannel();

		AtomicInteger msgsDelted = new AtomicInteger(0);

		if (message.getMentionedMembers().isEmpty()) {
			channel.sendMessage("You need to mention someone to clear messages from them").queue();
			return;
		}

		message.getMentionedMembers().forEach(member -> channel.getIterableHistory()
			.takeAsync(1000)
			.thenApply(list -> list.stream()
				.filter(message1 -> Objects.equals(message1.getMember(), member))
				.collect(Collectors.toList()))
			.thenApply(list -> Lists.partition(list, 100))
			.thenAccept(lists -> lists.forEach(list -> {
				msgsDelted.addAndGet(list.size());
				channel.purgeMessages(list);
			})).thenRun(() -> channel.sendMessage(getClearUserEmbed(msgsDelted.get(), member, event.getAuthor())).queue()));


	}

	private static MessageEmbed getClearUserEmbed(int numbers, Member deletingFrom, User author) {
		return new EmbedCreator()
			.setUser(deletingFrom.getUser())
			.setColor(Color.GREEN)
			.setTitle("Deleted Messages")
			.addField("Deleted " + numbers + " messages", "Messages deleted from " + deletingFrom.getAsMention())
			.create(author);
	}

	@Override
	public @NotNull String getName() {
		return "userclear";
	}

	@Override
	public Permission[] getNeededPermissions() {
		return new Permission[]{Permission.MESSAGE_MANAGE};
	}
}
