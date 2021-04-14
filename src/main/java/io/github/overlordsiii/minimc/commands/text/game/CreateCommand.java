package io.github.overlordsiii.minimc.commands.text.game;

import java.awt.Color;

import io.github.overlordsiii.minimc.Main;
import io.github.overlordsiii.minimc.api.EmbedCreator;
import io.github.overlordsiii.minimc.api.command.TextCommand;
import io.github.overlordsiii.minimc.api.AmongUsGame;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

public class CreateCommand implements TextCommand {





	public void execute(MessageReceivedEvent event) {

		Message message = event.getMessage();


		if (Main.currentGame != null) {

			MessageEmbed embed = new EmbedCreator()
				.setUser(event.getAuthor())
				.addErrorEmbed()
				.addField("Cannot Create Game", "Reason: You can't start a game if there is one already in progress")
				.addLink(Main.currentGame.getMessage())
				.mentionUser("Game Author", Main.currentGame.getAuthor())
				.create(event.getAuthor());

			message
				.reply(embed)
				.mentionRepliedUser(false)
				.queue();
			return;
		}

		MessageChannel channel = event.getChannel();

		MessageEmbed embed = new EmbedCreator()
			.setUser(message.getAuthor())
			.setColor(Color.GREEN)
			.setTitle("Start a game of Among Us!")
			.addField("How to Participate", "React to this message with the reaction to indicate you are playing this game.")
			.addField("How to Start", "Then run the command `!start` to begin")
			.create(message.getAuthor());

		channel.sendMessage(embed).queue(embedMessage -> {

			Main.currentGame = new AmongUsGame(embedMessage, message.getAuthor());


			embedMessage.addReaction("\uD83D\uDE80").queue();

			Main.currentGame.addUser(event.getAuthor());
			Main.currentGame.getMessage().editMessage(RocketReactionCommand.getEmbed()).queue();
		});
	}


	@Override
	public @NotNull String getName() {
		return "create";
	}
}
