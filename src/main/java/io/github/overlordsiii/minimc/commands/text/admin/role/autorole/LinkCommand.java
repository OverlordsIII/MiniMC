package io.github.overlordsiii.minimc.commands.text.admin.role.autorole;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

import io.github.overlordsiii.minimc.Main;
import io.github.overlordsiii.minimc.api.EmbedCreator;
import io.github.overlordsiii.minimc.api.command.TextCommand;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

public class LinkCommand implements TextCommand {
	@Override
	public void execute(MessageReceivedEvent event) {
		Message message = event.getMessage();

		Guild guild = event.getGuild();

		Member member = event.getMember();

		User user = event.getAuthor();

		List<Emote> emotes = message.getEmotes();

		List<Role> roles = message.getMentionedRoles();

		if (roles.size() != 1) {
			throw new IllegalArgumentException("You cannot link multiple roles to an emoji! Please mention only one role and one emoji in your `!link` command");
		}

		if (emotes.size() != 1) {
			throw new IllegalArgumentException("You cannot link multiple emojis to a role! Please use only one emoji and role in your `!link` command");
		}

		Emote emote = emotes.get(0);

		Role role = roles.get(0);

		Main.EMOTE_TO_ROLE.getObj().addProperty(Long.toString(emote.getIdLong()), role.getIdLong());

		try {
			Main.EMOTE_TO_ROLE.save();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Could not save emote config!\nReason: " + e);
		}

		MessageEmbed embed = new EmbedCreator()
			.setColor(Color.GREEN)
			.setTitle("Linked Role To Emoji")
			.appendDescription("Linked " + emote.getAsMention() + " to " + role.getAsMention())
			.setIcon(emote.getImageUrl())
			.setUser(event.getAuthor())
			.create(event.getAuthor());

		event.getChannel().sendMessage(embed).queue();


	}

	@NotNull
	@Override
	public String getName() {
		return "link";
	}
}
