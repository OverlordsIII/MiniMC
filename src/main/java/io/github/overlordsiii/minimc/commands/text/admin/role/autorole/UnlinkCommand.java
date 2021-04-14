package io.github.overlordsiii.minimc.commands.text.admin.role.autorole;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

import io.github.overlordsiii.minimc.Main;
import io.github.overlordsiii.minimc.api.EmbedCreator;
import io.github.overlordsiii.minimc.api.command.TextCommand;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

public class UnlinkCommand implements TextCommand {
	@Override
	public void execute(MessageReceivedEvent event) {
		List<Emote> emote = event.getMessage().getEmotes();



		emote.forEach(emote1 -> {
			Main.EMOTE_TO_ROLE.getObj().remove(Long.toString(emote1.getIdLong()));

			MessageEmbed embed = new EmbedCreator()
				.setColor(Color.GRAY)
				.setUser(event.getAuthor())
				.setTitle("Unlinked Emote")
				.appendDescription("Unlinked emote " + emote1.getAsMention())
				.create(event.getAuthor());

			event.getChannel().sendMessage(embed).queue();

			try {
				Main.EMOTE_TO_ROLE.save();
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException("Could not save emote config!\nReason: " + e);
			}
		});

	}

	@NotNull
	@Override
	public String getName() {
		return "unlink";
	}
}
