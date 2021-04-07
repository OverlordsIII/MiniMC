package io.github.overlordsiii.minimc.commands.join;

import java.util.List;

import io.github.overlordsiii.minimc.Main;
import io.github.overlordsiii.minimc.api.command.BaseCommand;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;

public class AddDefaultRoleCommand implements BaseCommand<GuildMemberJoinEvent> {
	@Override
	public void execute(GuildMemberJoinEvent event) {
		List<Role> roles = event.getGuild().getRolesByName(Main.CONFIG.getConfigOption("defaultRole"), true);

		Member member = event.getMember();

		roles.forEach(role -> {
			event.getGuild().addRoleToMember(member, role).queue();
		});
	}
}
