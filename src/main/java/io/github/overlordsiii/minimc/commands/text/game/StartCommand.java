package io.github.overlordsiii.minimc.commands.text.game;

import java.util.List;
import java.util.Random;


import io.github.overlordsiii.minimc.Main;
import io.github.overlordsiii.minimc.api.command.TextCommand;
import io.github.overlordsiii.minimc.util.EmbedUtil;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class StartCommand implements TextCommand {

	private static final Random RANDOM = new Random();

	public void execute(MessageReceivedEvent event) {

		MessageChannel channel = event.getChannel();

		Message message = event.getMessage();

		if (!message.getContentRaw().equalsIgnoreCase("!start")) {
			return;
		}
		if (Main.currentGame == null) {
			message.reply(EmbedUtil.getCannotCreateEmbed(event.getAuthor(), null, null, "Cannot start a game if there was no game created!")).queue();
			return;
		}

		if (Main.currentGame.getAuthor().getIdLong() != message.getAuthor().getIdLong()) {
			message.reply(EmbedUtil.getCannotCreateEmbed(event.getAuthor(), Main.currentGame.getAuthor(), Main.currentGame.getMessage(), "You cannot start the game since you are not the author!")).queue();
			return;
		}

		if (Main.currentGame.getPlayingUsers().size() < 2) {
			message.reply(EmbedUtil.getCannotCreateEmbed(event.getAuthor(), Main.currentGame.getAuthor(), Main.currentGame.getMessage(), "Cannot start the game with only one person playing")).queue();
			return;
		}

		channel.sendMessage("Dming Users...").queue();

		List<User> playing = Main.currentGame.getPlayingUsers();

		filterAndDmImposters(playing);
		playing.forEach(user -> sendPrivateMessage(user, EmbedUtil.createCrewmateEmbedSpec(user)));


		Main.currentGame = null;


	}

	private static void filterAndDmImposters(List<User> users) {

		int imposters = Main.CONFIG.getConfigOption("imposters", Integer::parseInt);


		for (int i = 0; i < imposters; i++) {
			int random = RANDOM.nextInt(users.size());
			User imposter = users.get(random);
			sendPrivateMessage(imposter, EmbedUtil.createImposterEmbedSpec(imposter));
			users.remove(random);
		}

	}


	public static void sendPrivateMessage(User user, MessageEmbed content) {
		user
			.openPrivateChannel()
			.submit()
			.thenCompose(channel -> channel.sendMessage(content).submit())
			.whenComplete((message, throwable) -> {
				if (throwable != null) throwable.printStackTrace();
			});

	}


	@Override
	public String getName() {
		return "start";
	}
}
