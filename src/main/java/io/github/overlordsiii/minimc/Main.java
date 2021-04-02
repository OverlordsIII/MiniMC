package io.github.overlordsiii.minimc;

import javax.security.auth.login.LoginException;

import io.github.overlordsiii.minimc.api.CommandHandler;
import io.github.overlordsiii.minimc.commands.text.admin.clear.ClearUserCommand;
import io.github.overlordsiii.minimc.commands.text.admin.kick.BanCommand;
import io.github.overlordsiii.minimc.commands.text.admin.kick.KickCommand;
import io.github.overlordsiii.minimc.commands.log.LogDeleteMessage;
import io.github.overlordsiii.minimc.commands.text.admin.clear.ClearCommand;
import io.github.overlordsiii.minimc.commands.text.admin.mute.MuteCommand;
import io.github.overlordsiii.minimc.commands.join.MuteOnJoinCommand;
import io.github.overlordsiii.minimc.commands.text.admin.mute.UnmuteCommand;
import io.github.overlordsiii.minimc.commands.text.status.ActivityCommand;
import io.github.overlordsiii.minimc.commands.text.status.ActivityTypeCommand;
import io.github.overlordsiii.minimc.commands.text.status.StatusCommand;
import io.github.overlordsiii.minimc.config.JsonHandler;
import io.github.overlordsiii.minimc.config.PropertiesHandler;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.hooks.InterfacedEventManager;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

public class Main {

	private static final PropertiesHandler TOKEN = PropertiesHandler.builder()
		.setFileName("token.properties")
		.setComment("This file sets the token for this bot")
		.addConfigOption("token", "")
		.build();

	public static final CommandHandler COMMAND_HANDLER = CommandHandler.builder()
		.addTextCommand(new StatusCommand())
		.addTextCommand(new ActivityCommand())
		.addTextCommand(new ActivityTypeCommand())
		.addTextCommand(new MuteCommand())
		.addTextCommand(new UnmuteCommand())
		.addTextCommand(new KickCommand())
		.addTextCommand(new BanCommand())
		.addTextCommand(new ClearCommand())
		.addTextCommand(new ClearUserCommand())
		.addJoinCommand(new MuteOnJoinCommand())
		.addMsgDeleteCommand(new LogDeleteMessage())
		.build();

	public static final JsonHandler MUTED_CONFIG = new JsonHandler("mutedMembers.json")
		.initialize();

	public static JDA JDA;

	public static final PropertiesHandler CONFIG = PropertiesHandler.builder()
		.setFileName("mcs.properties")
		.setComment("Sets the basic configuration properties for the mcs bot")
		.addConfigOption("status", OnlineStatus.ONLINE.toString())
		.addConfigOption("activityType", Activity.ActivityType.DEFAULT.toString())
		.addConfigOption("activity", "KING OF DND")
		.addConfigOption("mutedRole", "Muted")
		.addConfigOption("botLog", "bot-log")
		.build();


	public static void main(String[] args) throws LoginException {
		JDA = JDABuilder
			.createDefault(TOKEN.getConfigOption("token"))
			.setEventManager(new InterfacedEventManager())
			.addEventListeners(COMMAND_HANDLER)
			.setMemberCachePolicy(MemberCachePolicy.ALL)
			.enableIntents(GatewayIntent.GUILD_MEMBERS)
			.setActivity(Activity.of(CONFIG.getConfigOption("activityType", Activity.ActivityType::valueOf), CONFIG.getConfigOption("activity")))
			.setStatus(CONFIG.getConfigOption("status", OnlineStatus::valueOf))
			.build();
	}
}
