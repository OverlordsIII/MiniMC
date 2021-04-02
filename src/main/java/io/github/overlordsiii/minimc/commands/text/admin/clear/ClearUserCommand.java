package io.github.overlordsiii.minimc.commands.text.admin.clear;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import io.github.overlordsiii.minimc.api.command.TextCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

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
			})).thenRun(() -> channel.sendMessage("Removed " + msgsDelted.get() + " messages from " + member.getAsMention()).queue()));


	}

	@Override
	public String getName() {
		return "userclear";
	}

	@Override
	public Permission[] getNeededPermissions() {
		return new Permission[]{Permission.MESSAGE_MANAGE};
	}
}
