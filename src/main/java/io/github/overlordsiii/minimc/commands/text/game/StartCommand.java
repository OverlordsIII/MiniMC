package io.github.overlordsiii.minimc.commands.text.game;

import java.awt.Color;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.overlordsiii.minimc.Start;
import io.github.overlordsiii.minimc.api.EmbedCreator;
import io.github.overlordsiii.minimc.api.GuildExtension;
import io.github.overlordsiii.minimc.api.command.TextCommand;
import io.github.overlordsiii.minimc.config.JsonHandler;
import io.github.overlordsiii.minimc.config.PropertiesHandler;
import net.dv8tion.jda.api.entities.Guild;
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

		GuildExtension extension = Start.GUILD_MANAGER.getExtension(event.getGuild());

		JsonHandler handler = extension.getAmongusConfig();

		JsonObject object = handler.getObj();

		if (!object.has("message")) {

			MessageEmbed embed = new EmbedCreator()
				.setUser(event.getAuthor())
				.addErrorEmbed()
				.addField("Cannot Create Game!", "Reason: Cannot start a game if there was no game created!")
				.create(event.getAuthor());

			message.reply(embed).queue();
			return;
		}

		event.getChannel().retrieveMessageById(object.get("message").getAsLong()).queue(gameMessage -> {

			User user = event.getGuild().getMemberById(object.get("author").getAsLong()).getUser();


			if (object.get("author").getAsLong() != message.getAuthor().getIdLong()) {

				MessageEmbed embed = new EmbedCreator()
					.setUser(event.getAuthor())
					.addErrorEmbed()
					.addField("Cannot Start Game!", "Reason: You cannot start the game since you are not the author!")
					.addLink(gameMessage)
					.mentionUser("Game Creator", user)
					.create(event.getAuthor());


				message.reply(embed).queue();
				return;
			}

			JsonArray array = object.get("players").getAsJsonArray();



			if (RocketReactionCommand.getUsers(array, event.getGuild()).size() < 2) {

				MessageEmbed embed = new EmbedCreator()
					.setUser(event.getAuthor())
					.addErrorEmbed()
					.addField("Cannot Start Game!", "Reason: Cannot start game with only one person playing!")
					.addLink(gameMessage)
					.mentionUser("Game Creator", user)
					.create(event.getAuthor());

				message.reply(embed).queue();
				return;
			}

			channel.sendMessage("Dming Users...").queue();

			List<User> playing = RocketReactionCommand.getUsers(array, event.getGuild());

			filterAndDmImposters(playing, event.getAuthor(), event.getGuild());



			playing.forEach(player -> sendPrivateMessage(player, new EmbedCreator()
				.setColor(Color.CYAN)
				.setUser(player)
				.setTitle("You are a crewmate!")
				.addField("Your Job", "You are to find the imposter and win the bedwars game. Try not to die and win! If you think you see the imposter, vote them out at the gen upgrade")
				.create(event.getAuthor())));


			Set<Map.Entry<String, JsonElement>> elemnts = object.deepCopy().entrySet();

			elemnts.forEach(stringJsonElementEntry -> object.remove(stringJsonElementEntry.getKey()));



			try {
				handler.save();
				handler.load();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		});



	}

	private static void filterAndDmImposters(List<User> users, User executor, Guild guild) {

		PropertiesHandler handler = Start.GUILD_MANAGER.getExtension(guild).getGuildProperties();

		int imposters = handler.getConfigOption("imposters", Integer::parseInt);


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
