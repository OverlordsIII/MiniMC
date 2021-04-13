package io.github.overlordsiii.minimc.commands.join;

import java.awt.Color;
import java.util.List;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.overlordsiii.minimc.Main;
import io.github.overlordsiii.minimc.api.EmbedCreator;
import io.github.overlordsiii.minimc.api.MutedEntry;
import io.github.overlordsiii.minimc.api.command.BaseCommand;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;

public class MuteOnJoinCommand implements BaseCommand<GuildMemberJoinEvent> {
	@Override
	public void execute(GuildMemberJoinEvent event) {

		JsonObject object = Main.MUTED_CONFIG.getObj();

		Member member = event.getMember();

		List<Role> roles = event.getGuild().getRolesByName(Main.CONFIG.getConfigOption("Muted"), true);

		if (object.has(Long.toString(member.getIdLong()))) {
			JsonObject element = object.get(Long.toString(member.getIdLong())).getAsJsonObject();

			MutedEntry entry = MutedEntry.deserialize(element);

			MessageEmbed embed = new EmbedCreator()
				.setTitle("You Were Muted!")
				.setColor(Color.RED)
				.addField("Muted By Moderator", "Muted by " + idToMention(entry.getModerator()))
				.addField("Reason", entry.getReason())
				.create();

			member.getUser().openPrivateChannel()
				.queue(channel -> channel.sendMessage(embed).queue());

			roles.forEach(role -> event.getGuild().addRoleToMember(member, role).queue());


		}
	}


	private static String idToMention(long id) {
		String idString = Long.toString(id);
		idString = "<@!" + idString + ">";

		return idString;
	}

}
