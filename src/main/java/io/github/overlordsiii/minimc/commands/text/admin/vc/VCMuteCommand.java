package io.github.overlordsiii.minimc.commands.text.admin.vc;

import java.awt.Color;
import java.util.Objects;
import java.util.stream.Collectors;

import io.github.overlordsiii.minimc.api.EmbedCreator;
import io.github.overlordsiii.minimc.api.command.TextCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.IMentionable;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

public class VCMuteCommand implements TextCommand {
	@Override
	public void execute(MessageReceivedEvent event) {
		Member member = event.getMember();

		assert member != null;
		VoiceChannel channel = Objects.requireNonNull(member.getVoiceState()).getChannel();

		if (channel != null) {
			channel.getMembers().forEach(member1 -> member1.mute(true).queue());
			MessageEmbed embed = new EmbedCreator()
				.setColor(Color.GREEN)
				.setTitle("Muted Users!")
				.addField("Muted " + channel.getMembers().size() + " Users!", channel.getMembers()
					.stream()
					.map(IMentionable::getAsMention)
					.collect(Collectors.joining(", ")))
				.create(event.getAuthor());

			event.getChannel().sendMessage(embed).queue();
		}
	}

	@NotNull
	@Override
	public String getName() {
		return "vcmute";
	}

	@Override
	public Permission[] getNeededPermissions() {
		return new Permission[]{Permission.VOICE_MUTE_OTHERS};
	}
}
