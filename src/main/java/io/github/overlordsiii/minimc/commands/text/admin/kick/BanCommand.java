package io.github.overlordsiii.minimc.commands.text.admin.kick;

import io.github.overlordsiii.minimc.api.command.TextCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class BanCommand implements TextCommand {
	@Override
	public void execute(MessageReceivedEvent event) {
		event.getMessage().getMentionedMembers().forEach(member -> {
			member.ban(0).queue();
			event.getChannel().sendMessage("Banned " + member.getAsMention()).queue();
		});
	}

	@Override
	public Permission[] getNeededPermissions() {
		return new Permission[]{Permission.BAN_MEMBERS};
	}

	@Override
	public String getName() {
		return null;
	}
}
