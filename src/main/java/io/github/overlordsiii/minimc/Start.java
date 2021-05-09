package io.github.overlordsiii.minimc;

import javax.security.auth.login.LoginException;

import io.github.overlordsiii.minimc.api.command.CommandHandler;

import io.github.overlordsiii.minimc.commands.join.AddDefaultRoleCommand;
import io.github.overlordsiii.minimc.commands.join.MuteOnJoinCommand;
import io.github.overlordsiii.minimc.commands.join.WelcomeMessageCommand;
import io.github.overlordsiii.minimc.commands.log.LogDeleteMessage;
import io.github.overlordsiii.minimc.commands.text.admin.AnnounceCommand;
import io.github.overlordsiii.minimc.commands.text.admin.clear.ClearCommand;
import io.github.overlordsiii.minimc.commands.text.admin.clear.ClearUserCommand;
import io.github.overlordsiii.minimc.commands.text.admin.kick.BanCommand;
import io.github.overlordsiii.minimc.commands.text.admin.kick.KickCommand;
import io.github.overlordsiii.minimc.commands.text.admin.kick.UnbanCommand;
import io.github.overlordsiii.minimc.commands.text.admin.mute.MuteCommand;
import io.github.overlordsiii.minimc.commands.text.admin.mute.UnmuteCommand;
import io.github.overlordsiii.minimc.commands.text.admin.role.AddRoleCommand;
import io.github.overlordsiii.minimc.commands.text.admin.role.autorole.CreateMessageCommand;
import io.github.overlordsiii.minimc.commands.text.admin.role.autorole.EmoteAddedRoleCommand;
import io.github.overlordsiii.minimc.commands.text.admin.role.autorole.EmoteRemoveRoleCommand;
import io.github.overlordsiii.minimc.commands.text.admin.role.autorole.LinkCommand;
import io.github.overlordsiii.minimc.commands.text.admin.role.autorole.UnlinkCommand;
import io.github.overlordsiii.minimc.commands.text.admin.vc.VCMuteCommand;
import io.github.overlordsiii.minimc.commands.text.admin.vc.VCUnmuteCommand;
import io.github.overlordsiii.minimc.commands.text.fun.RandomChooseCommand;
import io.github.overlordsiii.minimc.commands.text.game.CreateCommand;
import io.github.overlordsiii.minimc.commands.text.game.GameLinkCommand;
import io.github.overlordsiii.minimc.commands.text.game.RocketReactionCommand;
import io.github.overlordsiii.minimc.commands.text.game.StartCommand;
import io.github.overlordsiii.minimc.commands.text.status.ActivityCommand;
import io.github.overlordsiii.minimc.commands.text.status.ActivityTypeCommand;
import io.github.overlordsiii.minimc.commands.text.status.StatusCommand;
import io.github.overlordsiii.minimc.config.GuildManager;
import io.github.overlordsiii.minimc.config.PropertiesHandler;
import io.github.overlordsiii.minimc.api.AmongUsGame;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.hooks.InterfacedEventManager;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

public class Start {

	private static final PropertiesHandler TOKEN = PropertiesHandler.builder()
		.setFileName("token.properties")
		.setComment("This file sets the token for this bot")
		.addConfigOption("token", "")
		.build();

	private static final PropertiesHandler API_KEY = PropertiesHandler.builder()
		.setFileName("apikey.properties")
		.setComment("This file sets the api key for hypixel operations")
		.addConfigOption("apiKey", "")
		.build();

	public static final CommandHandler COMMAND_HANDLER = CommandHandler.builder()
		.addTextCommand(new StatusCommand()) // change status of discord bot
		.addTextCommand(new ActivityCommand()) // change the activity of discord bot
		.addTextCommand(new ActivityTypeCommand()) // change the type of activity of the discord bot
		//.addTextCommand(new MuteCommand()) // mute a user
		//.addTextCommand(new UnmuteCommand()) // unmute a user
		//.addTextCommand(new KickCommand()) // kick a user
		//.addTextCommand(new BanCommand()) // ban a user
		//.addTextCommand(new UnbanCommand()) // unban a user
		//.addTextCommand(new ClearCommand()) // clear a certain amount of messages
		.addTextCommand(new ClearUserCommand()) // clear messages from a user in the last 100 messages
		//.addTextCommand(new AddRoleCommand()) // adds a role to a user
		//.addTextCommand(new LinkCommand())
		//.addTextCommand(new UnlinkCommand())
		//.addTextCommand(new CreateMessageCommand())
		.addTextCommand(new CreateCommand())
		.addTextCommand(new GameLinkCommand())
		.addTextCommand(new StartCommand())
		.addTextCommand(new VCMuteCommand())
		.addTextCommand(new VCUnmuteCommand())
		.addTextCommand(new RandomChooseCommand())
		.addTextCommand(new AnnounceCommand()) // sends a message in the announcement category
		//.addJoinCommand(new MuteOnJoinCommand()) // mutes a user if they were muted by the bot
		.addJoinCommand(new AddDefaultRoleCommand()) // adds a default role to a user if they joined the server
		//.addMsgDeleteCommand(new LogDeleteMessage()) // logs messages that are deleted
		.addReactionCommand(new RocketReactionCommand())
		//.addReactionCommand(new EmoteAddedRoleCommand())
		//.addReactionRemoveCommand(new EmoteRemoveRoleCommand())
		//.addJoinCommand(new WelcomeMessageCommand())
		.build();

	public static JDA JDA;

	public static final GuildManager GUILD_MANAGER = new GuildManager();

	public static AmongUsGame currentGame;

	public static final PropertiesHandler CONFIG = PropertiesHandler.builder()
		.setFileName("mcs.properties")
		.setComment("Sets the basic configuration properties for the mcs bot")
		.addConfigOption("status", OnlineStatus.ONLINE.toString())
		.addConfigOption("activityType", Activity.ActivityType.DEFAULT.toString())
		.addConfigOption("activity", "KING OF DND")
		.build();


	public static void main(String[] args) throws LoginException {
		JDA = JDABuilder
			.createDefault(TOKEN.getConfigOption("token"))
			.setEventManager(new InterfacedEventManager())
			.addEventListeners(COMMAND_HANDLER)
			.setMemberCachePolicy(MemberCachePolicy.ALL)
			.enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_EMOJIS, GatewayIntent.GUILD_VOICE_STATES)
			.setActivity(Activity.of(CONFIG.getConfigOption("activityType", Activity.ActivityType::valueOf), CONFIG.getConfigOption("activity")))
			.setStatus(CONFIG.getConfigOption("status", OnlineStatus::valueOf))
			.build();


	}
}
