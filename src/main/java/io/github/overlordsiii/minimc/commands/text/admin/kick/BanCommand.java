package io.github.overlordsiii.minimc.commands.text.admin.kick;

import java.awt.Color;

import io.github.overlordsiii.minimc.api.EmbedCreator;
import io.github.overlordsiii.minimc.api.command.TextCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

public class BanCommand implements TextCommand {
	@Override
	public void execute(MessageReceivedEvent event) {
		event.getMessage().getMentionedMembers().forEach(member -> {
			member.ban(0).queue();

			MessageEmbed embed = new EmbedCreator()
				.setColor(Color.CYAN)
				.setUser(member.getUser())
				.setTitle("Banned User")
				.mentionUser("Banned User", member.getUser())
				.create(event.getAuthor());

			event.getChannel().sendMessage(embed).queue();
		});
	}

	@Override
	public Permission[] getNeededPermissions() {
		return new Permission[]{Permission.BAN_MEMBERS};
	}

	@Override
	public @NotNull String getName() {
		return "ban";
	}
}
