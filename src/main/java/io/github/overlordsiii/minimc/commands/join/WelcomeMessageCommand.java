package io.github.overlordsiii.minimc.commands.join;

import java.awt.Color;

import io.github.overlordsiii.minimc.Start;
import io.github.overlordsiii.minimc.api.EmbedCreator;
import io.github.overlordsiii.minimc.api.command.BaseCommand;
import io.github.overlordsiii.minimc.config.PropertiesHandler;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;

public class WelcomeMessageCommand implements BaseCommand<GuildMemberJoinEvent> {
	@Override
	public void execute(GuildMemberJoinEvent event) {
		MessageEmbed embed = new EmbedCreator()
			.setUser(event.getUser())
			.setTitle("Welcome to " + event.getGuild().getName() + ", " + event.getUser().getName() + "!")
			.setColor(Color.GREEN)
			.create(event.getUser());

		PropertiesHandler handler = Start.GUILD_MANAGER.getGuildProperties().get(event.getGuild());

		TextChannel channel = event.getGuild().getTextChannelById(handler.getConfigOption("welcomeChannel"));

		channel.sendMessage(embed).queue();
	}
}
