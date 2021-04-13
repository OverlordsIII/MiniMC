package io.github.overlordsiii.minimc.commands.text.admin.kick;

import java.awt.Color;

import io.github.overlordsiii.minimc.api.EmbedCreator;
import io.github.overlordsiii.minimc.api.command.TextCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

public class UnbanCommand implements TextCommand {
	@Override
	public void execute(MessageReceivedEvent event) {
		event.getMessage().getMentionedUsers().forEach(user -> {
			event.getGuild().unban(user).queue();

			MessageEmbed embed = new EmbedCreator()
				.setColor(Color.CYAN)
				.setUser(user)
				.setTitle("Unbanned User")
				.addField("Unbanned User", user.getAsMention())
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
		return "unban";
	}
}
