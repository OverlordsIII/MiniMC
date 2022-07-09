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
import com.google.gson.JsonPrimitive;
import io.github.overlordsiii.minimc.Start;
import io.github.overlordsiii.minimc.api.EmbedCreator;
import io.github.overlordsiii.minimc.api.command.BaseCommand;
import io.github.overlordsiii.minimc.config.JsonHandler;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.IMentionable;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;

public class RocketReactionCommand implements BaseCommand<GuildMessageReactionAddEvent> {


	public void execute(GuildMessageReactionAddEvent event) {

		JsonHandler handler = Start.GUILD_MANAGER.getExtension(event.getGuild()).getAmongusConfig();

		JsonObject object = handler.getObj();

		if (!object.has("author") || event.getUser().isBot()) return;

		if (object.get("message").getAsLong() != event.getMessageIdLong()) return;

		if (event.getReactionEmote().isEmote() || !event.getReactionEmote().isEmoji()) return;

		if (!event.getReactionEmote().getEmoji().equals("\uD83D\uDE80")) return;

		JsonArray array1 = object.get("players").getAsJsonArray();


		User user = event.getGuild().getMemberById(object.get("author").getAsLong()).getUser();

		List<User> users = getUsers(array1, event.getGuild());

		if (users.contains(event.getUser())) {

			event.retrieveMessage().queue(message -> {
				message.removeReaction(event.getReactionEmote().getEmoji(), event.getUser()).queue();
				array1.remove(new JsonPrimitive(event.getUser().getIdLong()));
				try {
					handler.save();
					handler.load();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}

				message.editMessage(getEmbed(user, getUsers(handler.getObj().get("players").getAsJsonArray(), message.getGuild()))).queue();
			});
			return;
		}


		event.retrieveMessage().queue(message -> {
			message.removeReaction(event.getReactionEmote().getEmoji(), event.getUser()).queue();

			array1.add(new JsonPrimitive(event.getUser().getIdLong()));

			message.editMessage(getEmbed(user, getUsers(array1, event.getGuild()))).queue();
		});
	}

	public static MessageEmbed getEmbed(User author, List<User> playingUsers) {
		return new EmbedCreator()
			.setUser(author)
			.setColor(Color.GREEN)
			.setTitle("Start a game of Among Us!")
			.addField("How to Participate", "React to this message with the reaction to indicate you are playing this game.")
			.addField("How to Start", "Then run the command `!start` to begin")
			.addValuesAsField("Participants", IMentionable::getAsMention, playingUsers)
			.create(author);
	}

	public static List<User> getUsers(JsonArray array, Guild guild) {
		return StreamSupport.stream(array.spliterator(), false)
			.map(JsonElement::getAsLong)
			.map(guild::getMemberById)
			.filter(Objects::nonNull)
			.map(Member::getUser)
			.collect(Collectors.toList());
	}
}
