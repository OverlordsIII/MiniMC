package io.github.overlordsiii.minimc.commands.text.game;

import java.awt.Color;
import java.util.List;
import java.util.Random;


import io.github.overlordsiii.minimc.Main;
import io.github.overlordsiii.minimc.api.EmbedCreator;
import io.github.overlordsiii.minimc.api.command.TextCommand;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

public class StartCommand implements TextCommand {

	private static final Random RANDOM = new Random();

	public void execute(MessageReceivedEvent event) {

		MessageChannel channel = event.getChannel();

		Message message = event.getMessage();

		if (Main.currentGame == null) {

			MessageEmbed embed = new EmbedCreator()
				.setUser(event.getAuthor())
				.addErrorEmbed()
				.addField("Cannot Create Game!", "Reason: Cannot start a game if there was no game created!")
				.create(event.getAuthor());

			message.reply(embed).queue();
			return;
		}

		if (Main.currentGame.getAuthor().getIdLong() != message.getAuthor().getIdLong()) {

			MessageEmbed embed = new EmbedCreator()
				.setUser(event.getAuthor())
				.addErrorEmbed()
				.addField("Cannot Start Game!", "Reason: You cannot start the game since you are not the author!")
				.addLink(Main.currentGame.getMessage())
				.addField("Game Creator", Main.currentGame.getAuthor().getAsMention())
				.create(event.getAuthor());


			message.reply(embed).queue();
			return;
		}

		if (Main.currentGame.getPlayingUsers().size() < 2) {

			MessageEmbed embed = new EmbedCreator()
				.setUser(event.getAuthor())
				.addErrorEmbed()
				.addField("Cannot Start Game!", "Reason: Cannot start game with only one person playing!")
				.addLink(Main.currentGame.getMessage())
				.addField("Game Creator", Main.currentGame.getAuthor().getAsMention())
				.create(event.getAuthor());

			message.reply(embed).queue();
			return;
		}

		channel.sendMessage("Dming Users...").queue();

		List<User> playing = Main.currentGame.getPlayingUsers();

		filterAndDmImposters(playing, event.getAuthor());



		playing.forEach(user -> sendPrivateMessage(user, new EmbedCreator()
			.setColor(Color.CYAN)
			.setUser(user)
			.setTitle("You are a crewmate!")
			.addField("Your Job", "You are to find the imposter and win the bedwars game. Try not to die and win! If you think you see the imposter, vote them out at the gen upgrade")
			.create(event.getAuthor())));


		Main.currentGame = null;


	}

	private static void filterAndDmImposters(List<User> users, User executor) {

		int imposters = Main.CONFIG.getConfigOption("imposters", Integer::parseInt);


		for (int i = 0; i < imposters; i++) {
			int random = RANDOM.nextInt(users.size());
			User imposter = users.get(random);
			sendPrivateMessage(imposter, new EmbedCreator()
			.setColor(Color.RED)
			.setTitle("You are the Imposter!")
			.addField("Your Job", "You are to team grief your bedwars team without being noticed. If you are noticed, the crewmates can vote you out and possibly win the game")
			.create(executor));
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
	public @NotNull String getName() {
		return "start";
	}
}
