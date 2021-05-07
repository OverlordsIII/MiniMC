package io.github.overlordsiii.minimc.commands.join;

import java.awt.Color;
import java.util.List;

import io.github.overlordsiii.minimc.Start;
import io.github.overlordsiii.minimc.api.EmbedCreator;
import io.github.overlordsiii.minimc.api.command.BaseCommand;
import io.github.overlordsiii.minimc.config.PropertiesHandler;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;

public class AddDefaultRoleCommand implements BaseCommand<GuildMemberJoinEvent> {
	@Override
	public void execute(GuildMemberJoinEvent event) {

		PropertiesHandler handler = Start.GUILD_MANAGER.getGuildProperties().get(event.getGuild());

		Role role = event.getGuild().getRoleById(handler.getConfigOption("defaultRole"));

		Member member = event.getMember();


		assert role != null;
		event.getGuild().addRoleToMember(member, role).queue();

		MessageEmbed embed = new EmbedCreator()
			.setColor(Color.cyan)
			.setTitle("Added default role to user!")
			.setUser(event.getUser())
			.addField("Added role", role.getAsMention())
			.addField("To User", member.getAsMention())
			.create(Start.JDA.getSelfUser());

		TextChannel channel = event.getGuild().getTextChannelById(handler.getConfigOption("botLog"));

		assert channel != null;
		channel.sendMessage(embed).queue();


	}
}
