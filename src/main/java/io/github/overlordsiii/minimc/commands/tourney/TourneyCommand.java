package io.github.overlordsiii.minimc.commands.tourney;

import java.util.List;

import com.google.common.collect.Lists;
import io.github.overlordsiii.minimc.api.command.TextCommand;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

public class TourneyCommand implements TextCommand {
	@Override
	public void execute(MessageReceivedEvent event) {
		List<Member> members = event.getMessage().getMentionedMembers();

		List<List<Member>> membersPartioned = Lists.partition(members, 2);
	}

	@NotNull
	@Override
	public String getName() {
		return "tourney";
	}
}
