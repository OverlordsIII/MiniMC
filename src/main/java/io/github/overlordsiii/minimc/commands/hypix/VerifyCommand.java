package io.github.overlordsiii.minimc.commands.hypix;

import java.awt.Color;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import io.github.overlordsiii.minimc.Start;
import io.github.overlordsiii.minimc.api.EmbedCreator;
import io.github.overlordsiii.minimc.api.command.TextCommand;
import io.github.overlordsiii.minimc.config.JsonHandler;
import io.github.overlordsiii.minimc.util.MojangAPIUtil;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;

public class VerifyCommand implements TextCommand {

	private static final Gson GSON = new GsonBuilder()
		.disableHtmlEscaping()
		.setPrettyPrinting()
		.serializeNulls()
		.create();

	@Override
	public void execute(MessageReceivedEvent event) {
		Message message = event.getMessage();

		if (event.getMessage().getContentDisplay().trim().equals("!verify")) {
			throw new RuntimeException("Please put your ign as an argument!");
		}

		String ign = message.getContentDisplay().split("\\s+")[1];

		String uuid = MojangAPIUtil.ignToUuid(ign);

		Start.API.getPlayerByUuid(uuid).thenAccept(playerReply -> {
			JsonObject playerObj = playerReply.getPlayer();

			JsonObject links = playerObj.get("socialMedia").getAsJsonObject().get("links").getAsJsonObject();

			if (links.has("DISCORD")) {

				String discord = links.get("DISCORD").getAsString();


				if (discord.equals(event.getAuthor().getAsTag())) {

					JsonHandler handler = Start.GUILD_MANAGER.getExtension(event.getGuild()).getUuidConfig();

					JsonObject object = handler.getObj();

					if (object.has(event.getAuthor().getId())) {
						throw new RuntimeException("You are already verified! Run !unverify to unverify yourself first");
					}

					object.add(event.getAuthor().getId(), new JsonPrimitive(uuid));

					try {
						handler.save();
					} catch (IOException e) {
						e.printStackTrace();
					}

					MessageEmbed embed = new EmbedCreator()
						.setTitle("Verified Discord Account with Hypixel Account")
						.setColor(Color.CYAN)
						.setUser(Start.JDA.getSelfUser())
						.addField("Verified User to Hypixel", "Verified " + event.getAuthor().getAsMention() + " to " + ign)
						.create(event.getAuthor());

					event.getChannel().sendMessage(embed).queue();
				} else {
					MessageEmbed creator = new EmbedCreator()
						.setUser(Start.JDA.getSelfUser())
						.addErrorEmbed()
						.appendDescription("Could not verify discord as the hypixel user has set a different account as their discord!")
						.create(event.getAuthor());

					event.getChannel().sendMessage(creator).queue();
				}
			} else {

				MessageEmbed creator = new EmbedCreator()
					.setUser(Start.JDA.getSelfUser())
					.addErrorEmbed()
					.appendDescription("Could not verify discord as the hypixel user does not have a discord account attached to their hypixel data!")
					.create(event.getAuthor());

				event.getChannel().sendMessage(creator).queue();
			}
		});

		/*
		Start.API.getPlayerByName(ign).whenComplete((playerReply, throwable) -> {
			if (throwable != null) throwable.printStackTrace();

			JsonObject object = playerReply.getPlayer();

			String string = GSON.toJson(object);

			event.getChannel().sendMessage("```json\n" + string + "```").queue();
		});
		 */
	}

	@NotNull
	@Override
	public String getName() {
		return "verify";
	}
}
