package io.github.overlordsiii.minimc.commands.text.game;


import io.github.overlordsiii.minimc.Main;
import io.github.overlordsiii.minimc.api.command.BaseCommand;
import io.github.overlordsiii.minimc.util.EmbedUtil;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.SubscribeEvent;

public class RocketReactionCommand implements BaseCommand<GuildMessageReactionAddEvent> {


	public void execute(GuildMessageReactionAddEvent event) {

		if (Main.currentGame == null || event.getUser().isBot()) return;

		if (Main.currentGame.getMessage().getIdLong() != event.getMessageIdLong()) return;

		if (event.getReactionEmote().isEmote() || !event.getReactionEmote().isEmoji()) return;

		if (!event.getReactionEmote().getEmoji().equals("\uD83D\uDE80")) return;

		if (Main.currentGame.getPlayingUsers().contains(event.getUser())) {
			event.retrieveMessage().queue(message -> {
				message.removeReaction(event.getReactionEmote().getEmoji(), event.getUser()).queue();
				Main.currentGame.removeUser(event.getUser());
				Main.currentGame.getMessage().editMessage(EmbedUtil.createUpdatedEmbed(EmbedUtil.createDefaultEmbed(Main.currentGame.getAuthor()), Main.currentGame.getPlayingUsers())).queue();
			});
			return;
		}


		event.retrieveMessage().queue(message -> {
			message.removeReaction(event.getReactionEmote().getEmoji(), event.getUser()).queue();
			Main.currentGame.addUser(event.getUser());
			Main.currentGame.getMessage().editMessage(EmbedUtil.createUpdatedEmbed(EmbedUtil.createDefaultEmbed(Main.currentGame.getAuthor()), Main.currentGame.getPlayingUsers())).queue();
		});
	}
}
