package io.github.overlordsiii.minimc.commands.text.game;

import java.awt.Color;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.overlordsiii.minimc.Start;
import io.github.overlordsiii.minimc.api.EmbedCreator;
import io.github.overlordsiii.minimc.api.GuildExtension;
import io.github.overlordsiii.minimc.api.command.TextCommand;
import io.github.overlordsiii.minimc.api.AmongUsGame;
import io.github.overlordsiii.minimc.config.JsonHandler;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

public class CreateCommand implements TextCommand {





	public void execute(MessageReceivedEvent event) {

		Message message = event.getMessage();

		GuildExtension extension = Start.GUILD_MANAGER.getExtension(event.getGuild());

		JsonHandler handler = extension.getAmongusConfig();

		JsonObject object = handler.getObj();

		if (object.has("author")) {

			JsonObject obj = handler.getObj();

			User user = event.getGuild().getMemberById(obj.get("author").getAsLong()).getUser();

			event.getChannel().retrieveMessageById(obj.get("message").getAsLong()).queue(message1 -> {
				MessageEmbed embed = new EmbedCreator()
					.setUser(event.getAuthor())
					.addErrorEmbed()
					.addField("Cannot Create Game", "Reason: You can't start a game if there is one already in progress")
					.addLink(message1)
					.mentionUser("Game Author", user)
					.create(event.getAuthor());

				message
					.reply(embed)
					.mentionRepliedUser(false)
					.queue();
			});

			return;
		}

		/*
		if (Start.currentGame != null) {

			MessageEmbed embed = new EmbedCreator()
				.setUser(event.getAuthor())
				.addErrorEmbed()
				.addField("Cannot Create Game", "Reason: You can't start a game if there is one already in progress")
				.addLink(Start.currentGame.getMessage())
				.mentionUser("Game Author", Start.currentGame.getAuthor())
				.create(event.getAuthor());

			message
				.reply(embed)
				.mentionRepliedUser(false)
				.queue();
			return;
		}
		 */

		MessageChannel channel = event.getChannel();

		MessageEmbed embed = new EmbedCreator()
			.setUser(message.getAuthor())
			.setColor(Color.GREEN)
			.setTitle("Start a game of Among Us!")
			.addField("How to Participate", "React to this message with the reaction to indicate you are playing this game.")
			.addField("How to Start", "Then run the command `!start` to begin")
			.create(message.getAuthor());

		channel.sendMessage(embed).queue(embedMessage -> {

			object.addProperty("message", embedMessage.getIdLong());
			object.addProperty("author", message.getAuthor().getIdLong());


			embedMessage.addReaction("\uD83D\uDE80").queue();

			JsonArray array = new JsonArray();

			array.add(message.getAuthor().getIdLong());

			object.add("players", array);

			try {
				handler.save();
				handler.load();
			} catch (IOException e) {
				e.printStackTrace();
			}

			JsonObject object1 = handler.getObj();

			JsonArray array1 = object1.get("players").getAsJsonArray();

			List<User> users = StreamSupport.stream(array1.spliterator(), false)
				.map(JsonElement::getAsLong)
				.map(aLong -> event.getGuild().getMemberById(aLong))
				.filter(Objects::nonNull)
				.map(Member::getUser)
				.collect(Collectors.toList());




			embedMessage.editMessage(RocketReactionCommand.getEmbed(event.getAuthor(), users)).queue();
		});
	}


	@Override
	public @NotNull String getName() {
		return "create";
	}
}
