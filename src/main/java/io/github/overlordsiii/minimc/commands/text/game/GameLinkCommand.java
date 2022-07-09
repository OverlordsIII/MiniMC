package io.github.overlordsiii.minimc.commands.text.game;


import java.awt.Color;

import com.google.gson.JsonObject;
import io.github.overlordsiii.minimc.Start;
import io.github.overlordsiii.minimc.api.EmbedCreator;
import io.github.overlordsiii.minimc.api.GuildExtension;
import io.github.overlordsiii.minimc.api.command.TextCommand;
import io.github.overlordsiii.minimc.config.JsonHandler;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

public class GameLinkCommand implements TextCommand {


	public void execute(MessageReceivedEvent event) {

		Message message = event.getMessage();
		String content = message.getContentRaw();

		GuildExtension extension = Start.GUILD_MANAGER.getExtension(event.getGuild());

		JsonHandler jsonHandler = extension.getAmongusConfig();

		if (!jsonHandler.getObj().has("message")) {

			MessageEmbed embed = new EmbedCreator()
				.addErrorEmbed()
				.addField("Cannot Start Game!", "Reason: Cannot get the game link if there was no game created!")
				.setUser(event.getAuthor())
				.create(event.getAuthor());

			event.getMessage().reply(embed).queue();
			return;
		}

		JsonObject obj = jsonHandler.getObj();

		User user = event.getGuild().getMemberById(obj.get("author").getAsLong()).getUser();

		event.getChannel().retrieveMessageById(obj.get("message").getAsLong()).queue(message1 -> {
			MessageEmbed embed = new EmbedCreator()
				.setColor(Color.ORANGE)
				.setUser(event.getAuthor())
				.setTitle("Game Link")
				.addLink(message)
				.mentionUser("Game Creator", user)
				.create(event.getAuthor());

			event.getMessage().reply(embed).queue();
		});




	}

	@Override
	public @NotNull String getName() {
		return "gamelink";
	}
}
