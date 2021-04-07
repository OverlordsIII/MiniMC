package io.github.overlordsiii.minimc.commands.text.game;


import io.github.overlordsiii.minimc.Main;
import io.github.overlordsiii.minimc.api.command.TextCommand;
import io.github.overlordsiii.minimc.util.EmbedUtil;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class GameLinkCommand implements TextCommand {


	public void execute(MessageReceivedEvent event) {

		Message message = event.getMessage();
		String content = message.getContentRaw();

		if (Main.currentGame == null) {
			event.getMessage().reply(EmbedUtil.getCannotCreateEmbed(event.getAuthor(), null, null, "Cannot get the game link if there was no game created!")).queue();
			return;
		}

		event.getMessage().reply(EmbedUtil.getLinkEmbed(event.getAuthor(), Main.currentGame.getMessage(), Main.currentGame.getAuthor())).queue();
	}

	@Override
	public String getName() {
		return "gamelink";
	}
}
