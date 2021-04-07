package io.github.overlordsiii.minimc.commands.text.game;

import io.github.overlordsiii.minimc.Main;
import io.github.overlordsiii.minimc.api.command.TextCommand;
import io.github.overlordsiii.minimc.api.AmongUsGame;
import io.github.overlordsiii.minimc.util.EmbedUtil;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CreateCommand implements TextCommand {





	public void execute(MessageReceivedEvent event) {

		Message message = event.getMessage();


		if (Main.currentGame != null) {
			message
				.reply(EmbedUtil.getCannotCreateEmbed(event.getAuthor(), Main.currentGame.getAuthor(), Main.currentGame.getMessage(),
					"You can't start a game if there is already one in progress!"))
				.mentionRepliedUser(false)
				.queue();
			return;
		}

		MessageChannel channel = event.getChannel();


		channel.sendMessage(EmbedUtil.createDefaultEmbed(message.getAuthor()).build()).queue(embedMessage -> {

			Main.currentGame = new AmongUsGame(embedMessage, message.getAuthor());


			embedMessage.addReaction("\uD83D\uDE80").queue();

			Main.currentGame.addUser(event.getAuthor());
			Main.currentGame.getMessage().editMessage(EmbedUtil.createUpdatedEmbed(EmbedUtil.createDefaultEmbed(Main.currentGame.getAuthor()), Main.currentGame.getPlayingUsers())).queue();
		});
	}


	@Override
	public String getName() {
		return "create";
	}
}
