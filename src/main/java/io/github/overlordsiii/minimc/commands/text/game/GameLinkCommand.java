package io.github.overlordsiii.minimc.commands.text.game;


import java.awt.Color;

import io.github.overlordsiii.minimc.Main;
import io.github.overlordsiii.minimc.api.EmbedCreator;
import io.github.overlordsiii.minimc.api.command.TextCommand;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class GameLinkCommand implements TextCommand {


	public void execute(MessageReceivedEvent event) {

		Message message = event.getMessage();
		String content = message.getContentRaw();

		if (Main.currentGame == null) {

			MessageEmbed embed = new EmbedCreator()
				.addErrorEmbed()
				.addField("Cannot Start Game!", "Reason: Cannot get the game link if there was no game created!")
				.setUser(event.getAuthor())
				.create(event.getAuthor());

			event.getMessage().reply(embed).queue();
			return;
		}

		MessageEmbed embed = new EmbedCreator()
			.setColor(Color.ORANGE)
			.setUser(event.getAuthor())
			.setTitle("Game Link")
			.addLink(Main.currentGame.getMessage())
			.addField("Game Creator", Main.currentGame.getAuthor().getAsMention())
			.create(event.getAuthor());

		event.getMessage().reply(embed).queue();
	}

	@Override
	public String getName() {
		return "gamelink";
	}
}
