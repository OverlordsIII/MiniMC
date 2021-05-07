package io.github.overlordsiii.minimc.commands.text.fun;

import java.awt.Color;
import java.util.List;
import java.util.Random;

import io.github.overlordsiii.minimc.api.EmbedCreator;
import io.github.overlordsiii.minimc.api.command.TextCommand;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

public class RandomChooseCommand implements TextCommand {
	@Override
	public void execute(MessageReceivedEvent event) {
		List<Member> members = event.getMessage().getMentionedMembers();

		Random random = new Random();

		int randomInt = random.nextInt(members.size());

		Member chosenOne = members.get(randomInt);

		MessageEmbed embed = new EmbedCreator()
			.setColor(Color.CYAN)
			.setTitle("A Winner was chosen!")
			.appendDescription("The Winner was " + chosenOne.getAsMention())
			.create(event.getAuthor());

		event.getChannel().sendMessage(embed).queue();
	}

	@NotNull
	@Override
	public String getName() {
		return "choose";
	}
}
