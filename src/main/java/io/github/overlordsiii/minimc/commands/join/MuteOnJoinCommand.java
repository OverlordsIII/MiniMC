package io.github.overlordsiii.minimc.commands.join;

import java.util.List;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.overlordsiii.minimc.Main;
import io.github.overlordsiii.minimc.api.MutedEntry;
import io.github.overlordsiii.minimc.api.command.BaseCommand;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;

public class MuteOnJoinCommand implements BaseCommand<GuildMemberJoinEvent> {
	@Override
	public void execute(GuildMemberJoinEvent event) {

		JsonObject object = Main.MUTED_CONFIG.getObj();

		Member member = event.getMember();

		List<Role> roles = event.getGuild().getRolesByName("Muted", true);

		if (object.has(Long.toString(member.getIdLong()))) {
			JsonObject element = object.get(Long.toString(member.getIdLong())).getAsJsonObject();

			MutedEntry entry = MutedEntry.deserialize(element);

			member.getUser().openPrivateChannel()
				.queue(channel -> channel.sendMessage("You were muted by " + idToMention(entry.getModerator()) + " because \"" + entry.getReason() + "\"").queue());

			roles.forEach(role -> event.getGuild().addRoleToMember(member, role).queue());


		}
	}


	private static String idToMention(long id) {
		String idString = Long.toString(id);
		idString = "<@!" + idString + ">";

		return idString;
	}

}
