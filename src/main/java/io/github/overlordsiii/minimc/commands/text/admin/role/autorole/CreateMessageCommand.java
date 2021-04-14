package io.github.overlordsiii.minimc.commands.text.admin.role.autorole;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.google.gson.JsonObject;
import io.github.overlordsiii.minimc.Main;
import io.github.overlordsiii.minimc.api.EmbedCreator;
import io.github.overlordsiii.minimc.api.command.TextCommand;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.internal.utils.Helpers;
import org.jetbrains.annotations.NotNull;

public class CreateMessageCommand implements TextCommand {
	@Override
	public void execute(MessageReceivedEvent event) {

		System.out.println("triggered");

		Message message = event.getMessage();

		Guild guild = event.getGuild();

		Member member = event.getMember();

		User user = event.getAuthor();

		JsonObject object = Main.EMOTE_TO_ROLE.getObj();

		EmbedCreator creator = new EmbedCreator()
			.setTitle("Role Selector")
			.setColor(Color.CYAN)
			.setUser(Main.JDA.getSelfUser());



		guild.getTextChannelsByName(Main.CONFIG.getConfigOption("roleChannel"), true).forEach(textChannel -> {

			List<Emote> emotes = new ArrayList<>();

			object.entrySet().forEach(stringJsonElementEntry -> {
				String emoteId = stringJsonElementEntry.getKey();

				String roleId = stringJsonElementEntry.getValue().getAsString();

				if (Helpers.isNumeric(emoteId) && Helpers.isNumeric(roleId)) {
					Emote emote = guild.getEmoteById(emoteId);

					Role role = guild.getRoleById(roleId);

					Objects.requireNonNull(emote);
					Objects.requireNonNull(role);

					addRoleAndEmoji(creator, emote, role);

					emotes.add(emote);

				}
			});

			textChannel.sendMessage(creator.create(event.getAuthor())).queue(messager -> {

				if (object.has("messageId")) {

					long msgId = object.get("messageId").getAsLong();

					textChannel.retrieveMessageById(msgId)
						.queue(message1 -> message1.delete().queue(), t -> {} );

					object.remove("messageId");
				}
				Main.EMOTE_TO_ROLE.getObj().addProperty("messageId", messager.getIdLong());

				emotes.forEach(emote -> messager.addReaction(emote).queue());

				try {
					Main.EMOTE_TO_ROLE.save();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}

			});
		});
	}

	private static void addRoleAndEmoji(EmbedCreator creator, Emote emote, Role role) {
		creator.appendDescription(emote.getAsMention() + " - " + role.getAsMention());
		creator.appendDescription("\n");
	}

	@NotNull
	@Override
	public String getName() {
		return "createMessage";
	}
}
