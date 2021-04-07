package io.github.overlordsiii.minimc.util;

import java.awt.Color;
import java.util.List;

import io.github.overlordsiii.minimc.api.AmongUsGame;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;

public class EmbedUtil {
	public static MessageEmbed createCrewmateEmbedSpec(User participant) {

		EmbedBuilder builder = new EmbedBuilder();

		//builder.setImage("https://micky.com.au/wp-content/uploads/2020/10/fb-innersloth-among-us-crewmates.jpg");
		builder.setColor(Color.CYAN);
		builder.setTitle("You are a Crewmate!");
		builder.addField("Your Job", "You are to find the imposter and win the bedwars game. Try not to die and win! If you think you see the imposter, vote them out at the gen upgrade", false);
		builder.setAuthor(participant.getName(), participant.getAvatarUrl(), participant.getAvatarUrl());

		return builder.build();
	}

	public static MessageEmbed createImposterEmbedSpec(User participant) {

		EmbedBuilder builder = new EmbedBuilder();

		//builder.setImage("https://th.bing.com/th/id/OIP.dk5-h6vwUeznfH_-dDsivAHaEK?pid=ImgDet&rs=1");
		builder.setColor(Color.RED);
		builder.setTitle("You are the Imposter!");
		builder.addField("Your Job", "You are to team grief your bedwars team without being noticed. If you are noticed, the crewmates can vote you out and possibly win the game", false);
		builder.setAuthor(participant.getName(), participant.getAvatarUrl(), participant.getAvatarUrl());

		return builder.build();
	}
	public static MessageEmbed getCannotCreateEmbed(User requestor, User gameAuthor, Message gameMessage, String reason) {
		EmbedBuilder builder = new EmbedBuilder();
		builder.setAuthor(requestor.getName(), requestor.getAvatarUrl(), requestor.getAvatarUrl());
		builder.setColor(Color.RED);
		builder.setTitle("Cannot create game!");
		builder.addField("Reason", reason, false);
		if (gameAuthor != null) {
			addGameCreator(builder, gameAuthor);
		}
		if (gameMessage != null) {
			addGameLink(builder, gameMessage);
		}
		return builder.build();
	}

	public static void addGameCreator(EmbedBuilder builder, User creator) {
		builder.addField("Game Creator", creator.getAsMention(), false);
	}

	public static void addGameLink(EmbedBuilder builder, Message gameMessage) {
		builder.addField("Link to Game", "[Jump!](" + AmongUsGame.getMessageLink(gameMessage) + ")", false);
	}

	public static MessageEmbed getLinkEmbed(User requestor, Message message, User gameAuthor) {
		EmbedBuilder builder = new EmbedBuilder();
		builder.setAuthor(requestor.getName(), requestor.getAvatarUrl(), requestor.getAvatarUrl());
		builder.setColor(Color.ORANGE);
		builder.setTitle("Game Link");
		addGameLink(builder, message);
		addGameCreator(builder, gameAuthor);
		return builder.build();
	}

	public static EmbedBuilder createDefaultEmbed(User executor) {
		EmbedBuilder builder = new EmbedBuilder();
		builder.setAuthor(executor.getName(), executor.getAvatarUrl(), executor.getAvatarUrl());
		builder.setColor(Color.GREEN);
		builder.setTitle("Start a game of among us!");
		builder.addField("How to Participate", "React to this message with the reaction to indicate you are playing this game.", false);
		builder.addField("How to Start", "Then run the command !start to begin!", false);
		return builder;
	}

	public static MessageEmbed createUpdatedEmbed(EmbedBuilder builder, List<User> participants) {

		if (participants.isEmpty()) {
			return builder.build();
		}

		StringBuilder stringBuilder = new StringBuilder();
		participants.forEach(user -> {
			stringBuilder.append(user.getAsMention());
			stringBuilder.append("\n");
		});
		builder.addField("Participants", stringBuilder.toString(), false);
		return builder.build();
	}
}
