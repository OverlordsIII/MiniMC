package io.github.overlordsiii.minimc.commands.text.admin.role;

import java.awt.Color;

import io.github.overlordsiii.minimc.api.EmbedCreator;
import io.github.overlordsiii.minimc.api.command.TextCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

public class AddRoleCommand implements TextCommand {
	@Override
	public void execute(MessageReceivedEvent event) {
		Message message = event.getMessage();

		Guild guild = event.getGuild();

		message.getMentionedMembers().forEach(member -> {
			StringBuilder builder = new StringBuilder();

			message.getMentionedRoles().forEach(role -> {
				guild.addRoleToMember(member, role).queue();

				builder.append(role.getAsMention());
				builder.append("\n");
			});

			MessageEmbed embed = new EmbedCreator()
				.setColor(Color.CYAN)
				.setTitle("Added Role")
				.setUser(member.getUser())
				.mentionUser("User that roles were added to", member.getUser())
				.addField("Roles added", builder.toString())
				.create(event.getAuthor());

			message.getChannel().sendMessage(embed).queue();
		});
	}

	@NotNull
	@Override
	public String getName() {
		return "addroles";
	}

	@Override
	public Permission[] getNeededPermissions() {
		return new Permission[]{Permission.MANAGE_ROLES};
	}
}
