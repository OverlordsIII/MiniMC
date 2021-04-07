package io.github.overlordsiii.minimc.api.command;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GenericGuildMessageReactionEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class CommandHandler extends ListenerAdapter {

	private final Set<TextCommand> textCommands;

	private final Set<BaseCommand<GuildMemberJoinEvent>> joinEvents;

	private final Set<BaseCommand<MessageDeleteEvent>> deleteCommands;

	private final Map<String, Message> sentMessages = new HashMap<>();

	private final Set<BaseCommand<GuildMessageReactionAddEvent>> reactionCommands;

	private CommandHandler(Set<TextCommand> commands, Set<BaseCommand<GuildMemberJoinEvent>> joinEvents, Set<BaseCommand<MessageDeleteEvent>> deleteCommands, Set<BaseCommand<GuildMessageReactionAddEvent>> reactionAddedCommands) {
		this.textCommands = commands;
		this.joinEvents = joinEvents;
		this.deleteCommands = deleteCommands;
		this.reactionCommands  = reactionAddedCommands;
	}

	public static Builder builder() {
		return new Builder();
	}

	public void onMessageReceived(@Nonnull MessageReceivedEvent event) {

		if (event.getAuthor().isBot()) return;

		sentMessages.put(event.getMessageId(), event.getMessage());

		Guild guild = event.getGuild();

		User author = event.getAuthor();

		Member member = guild.getMember(author);

		textCommands.forEach(command -> {

			if (!command.getPredicate().test(event)) return;

			String content = event.getMessage().getContentDisplay().toLowerCase(Locale.ROOT);

			if (content.trim().startsWith(command.getPrefix() + command.getName())) {

				if (command.getNeededPermissions() != null && member != null && !member.hasPermission(command.getNeededPermissions())) {
						event.getChannel().sendMessage("Cannot execute command \"" + command.getName() +"\" because you do not have the permissions required! (" + Arrays.toString(command.getNeededPermissions()) + ")").queue();
						return;
				}


				command.execute(event);
			}
		});
	}

	public Map<String, Message> getSentMessages() {
		return sentMessages;
	}

	@Override
	public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {

		joinEvents.forEach(eventBaseCommand -> {
			if (eventBaseCommand.getPredicate().test(event)) {
				if (eventBaseCommand.getNeededPermissions() != null) {
					if (!event.getMember().hasPermission(eventBaseCommand.getNeededPermissions())) {
						return;
					}
				}

				eventBaseCommand.execute(event);
			}
		});
	}

	@Override
	public void onGuildMessageReactionAdd(@NotNull GuildMessageReactionAddEvent event) {
		reactionCommands.forEach(command -> {
			if (command.getPredicate().test(event)) {
				command.execute(event);
			}
		});
	}

	@Override
	public void onMessageDelete(@NotNull MessageDeleteEvent event) {

		deleteCommands.forEach(eventBaseCommand -> {
			if (eventBaseCommand.getPredicate().test(event)) {

				eventBaseCommand.execute(event);
			}
		});
	}

	public static class Builder {

		private final Set<TextCommand> textCommands = new HashSet<>();

		private final Set<BaseCommand<GuildMemberJoinEvent>> joinCommands = new HashSet<>();

		private final Set<BaseCommand<MessageDeleteEvent>> deleteCommands = new HashSet<>();

		private final Set<BaseCommand<GuildMessageReactionAddEvent>> reactionAddedCommands = new HashSet<>();

		private Builder() {}

		public Builder addTextCommand(TextCommand command) {
			textCommands.add(command);

			return this;
		}

		public Builder addJoinCommand(BaseCommand<GuildMemberJoinEvent> eventBaseCommand) {
			joinCommands.add(eventBaseCommand);

			return this;
		}

		public Builder addMsgDeleteCommand(BaseCommand<MessageDeleteEvent> msgDeleteCommand) {
			deleteCommands.add(msgDeleteCommand);

			return this;
		}

		public Builder addReactionCommand(BaseCommand<GuildMessageReactionAddEvent> command) {
			reactionAddedCommands.add(command);

			return this;
		}

		public CommandHandler build() {
			return new CommandHandler(textCommands, joinCommands, deleteCommands, reactionAddedCommands);
		}

	}

	@Override
	public void onReady(@NotNull ReadyEvent event) {
		System.out.printf("Logged onto Minecrafter Society Bot on %s in %d servers", new Date().toString(), event.getJDA().getGuilds().size());
	}
}
