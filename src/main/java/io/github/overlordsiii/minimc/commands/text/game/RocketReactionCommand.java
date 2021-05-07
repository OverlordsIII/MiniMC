package io.github.overlordsiii.minimc.commands.text.game;


import java.awt.Color;

import io.github.overlordsiii.minimc.Start;
import io.github.overlordsiii.minimc.api.EmbedCreator;
import io.github.overlordsiii.minimc.api.command.BaseCommand;
import net.dv8tion.jda.api.entities.IMentionable;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;

public class RocketReactionCommand implements BaseCommand<GuildMessageReactionAddEvent> {


	public void execute(GuildMessageReactionAddEvent event) {

		if (Start.currentGame == null || event.getUser().isBot()) return;

		if (Start.currentGame.getMessage().getIdLong() != event.getMessageIdLong()) return;

		if (event.getReactionEmote().isEmote() || !event.getReactionEmote().isEmoji()) return;

		if (!event.getReactionEmote().getEmoji().equals("\uD83D\uDE80")) return;

		if (Start.currentGame.getPlayingUsers().contains(event.getUser())) {
			event.retrieveMessage().queue(message -> {
				message.removeReaction(event.getReactionEmote().getEmoji(), event.getUser()).queue();
				Start.currentGame.removeUser(event.getUser());

				Start.currentGame.getMessage().editMessage(getEmbed()).queue();
			});
			return;
		}


		event.retrieveMessage().queue(message -> {
			message.removeReaction(event.getReactionEmote().getEmoji(), event.getUser()).queue();
			Start.currentGame.addUser(event.getUser());
			Start.currentGame.getMessage().editMessage(getEmbed()).queue();
		});
	}

	public static MessageEmbed getEmbed() {
		return new EmbedCreator()
			.setUser(Start.currentGame.getAuthor())
			.setColor(Color.GREEN)
			.setTitle("Start a game of Among Us!")
			.addField("How to Participate", "React to this message with the reaction to indicate you are playing this game.")
			.addField("How to Start", "Then run the command `!start` to begin")
			.addValuesAsField("Participants", IMentionable::getAsMention, Start.currentGame.getPlayingUsers())
			.create(Start.currentGame.getAuthor());
	}
}
