package io.github.overlordsiii.minimc.commands.text.fun;

import io.github.overlordsiii.minimc.Start;
import io.github.overlordsiii.minimc.api.command.BaseCommand;
import io.github.overlordsiii.minimc.config.PropertiesHandler;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class MentionMeIMentionYou implements BaseCommand<GuildMessageReceivedEvent> {

	@Override
	public void execute(GuildMessageReceivedEvent event) {

		if (!event.getMessage().getMentionedUsers().contains(Start.JDA.getSelfUser())) {
			return;
		}

		PropertiesHandler handler = Start.GUILD_MANAGER.getExtension(event.getGuild()).getGuildProperties();


		if (handler.getConfigOption("spamChannel").equalsIgnoreCase(event.getChannel().getId())) {
			if (event.getMessage().getMentionedUsers().contains(Start.JDA.getSelfUser())) {
				event.getMessage()
					.reply("Why you mention me I mention you!")
					.mentionRepliedUser(true)
					.queue();
			}
		}
	}
}
