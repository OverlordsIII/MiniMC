package io.github.overlordsiii.minimc.commands.join;

import java.awt.Color;

import com.google.gson.JsonObject;
import io.github.overlordsiii.minimc.Start;
import io.github.overlordsiii.minimc.api.EmbedCreator;
import io.github.overlordsiii.minimc.api.GuildExtension;
import io.github.overlordsiii.minimc.api.MutedEntry;
import io.github.overlordsiii.minimc.api.command.BaseCommand;
import io.github.overlordsiii.minimc.config.JsonHandler;
import io.github.overlordsiii.minimc.config.PropertiesHandler;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;

public class MuteOnJoinCommand implements BaseCommand<GuildMemberJoinEvent> {
	@Override
	public void execute(GuildMemberJoinEvent event) {

		GuildExtension extension = Start.GUILD_MANAGER.getExtension(event.getGuild());

		JsonHandler jsonHandler = extension.getMutedConfig();

		JsonObject object = jsonHandler.getObj();

		Member member = event.getMember();

		PropertiesHandler handler = extension.getGuildProperties();

		Role roles = event.getGuild().getRoleById(handler.getConfigOption("mutedRole"));

		if (object.has(Long.toString(member.getIdLong()))) {
			JsonObject element = object.get(Long.toString(member.getIdLong())).getAsJsonObject();

			MutedEntry entry = MutedEntry.deserialize(element);

			EmbedCreator embed = new EmbedCreator()
				.setTitle("You Were Muted!")
				.setColor(Color.RED)
				.addField("Muted By Moderator", "Muted by " + idToMention(entry.getModerator()))
				.addField("Reason", entry.getReason());

			member.getUser().openPrivateChannel()
				.queue(channel -> channel.sendMessage(embed.create(event.getGuild().getMemberById(entry.getModerator()))).queue());

			TextChannel textChannel = event.getGuild().getTextChannelById(handler.getConfigOption("botLog"));

			textChannel.sendMessage(embed.setTitle("Member was auto-muted").create(event.getGuild().getMemberById(entry.getModerator()))).queue();

			//roles.forEach(role -> event.getGuild().addRoleToMember(member, role).queue());
			assert roles != null;
			event.getGuild().addRoleToMember(member, roles).queue();

		}
	}


	private static String idToMention(long id) {
		String idString = Long.toString(id);
		idString = "<@!" + idString + ">";

		return idString;
	}

}
