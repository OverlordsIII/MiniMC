package io.github.overlordsiii.minimc.commands.text.admin.kick;

import io.github.overlordsiii.minimc.api.command.TextCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class KickCommand implements TextCommand {
	@Override
	public void execute(MessageReceivedEvent event) {
		event.getMessage().getMentionedMembers().forEach(member -> {
			member.kick().queue();
			event.getChannel().sendMessage("Kicked " + member.getAsMention()).queue();
		});
	}

	@Override
	public String getName() {
		return "kick";
	}

	@Override
	public Permission[] getNeededPermissions() {
		return new Permission[]{Permission.KICK_MEMBERS};
	}
}
