package io.github.overlordsiii.minimc.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.requests.restaction.pagination.ReactionPaginationAction;

public class AmongUsGame {

	private final Message message;
	private final User author;
	private final List<User> playingUsers = new ArrayList<>();

	public AmongUsGame(Message message, User authorId) {
		this.message = message;
		this.author = authorId;
	}

	public List<User> getPlayingUsers() {
		return playingUsers;
	}

	public void addUser(User user) {
		playingUsers.add(user);
	}

	public void removeUser(User user) {
		playingUsers.remove(user);
	}
	public Message getMessage() {
		return message;
	}

	public User getAuthor() {
		return author;
	}

}
