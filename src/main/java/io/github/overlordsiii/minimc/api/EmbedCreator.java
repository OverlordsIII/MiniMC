package io.github.overlordsiii.minimc.api;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Icon;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;

public class EmbedCreator {

	private final EmbedBuilder builder;

	public EmbedCreator(EmbedBuilder builder) {
		this.builder = builder;
	}

	public EmbedCreator() {
		this(new EmbedBuilder());
	}

	public EmbedCreator addErrorEmbed() {
		this.builder
			.setColor(Color.RED)
			.setTitle("An Error Occurred!");

		return this;
	}

	public EmbedCreator setUser(User user) {

		if (user == null) return this;

		this.builder
			.setAuthor(user.getName(), user.getAvatarUrl(), user.getAvatarUrl());

		return this;
	}

	public EmbedCreator addField(String name, String value) {

		this.builder
			.addField(name, value, false);

		return this;
	}

	public EmbedCreator addValuesAsField(String name, String... values) {

		if (values == null || values.length == 0) return this;

		StringBuilder builder = new StringBuilder();

		for (String value : values) {
			builder.append(value);
			builder.append("\n");
		}

		this.builder
			.addField(name, builder.toString(), false);
		return this;
	}

	public EmbedCreator addAsMultipleFields(String name, String... values) {

		this.builder.addField(name, values[0], false);

		List<String> list = new ArrayList<>(Arrays.asList(values));

		list.remove(values[0]);

		for (String value : list) {
			this.builder.addField("", value, false);
		}

		return this;
	}

	@SafeVarargs
	public final <T> EmbedCreator addValuesAsField(String name, Function<T, String> converter, T... unparsed) {
		return addValuesAsField(name, Arrays.stream(unparsed)
			.map(converter == null ? Object::toString : converter)
			.toArray(String[]::new));
	}

	public final <T> EmbedCreator addValuesAsField(String name, Function<T, String> converter, List<T> unparsed) {
		return addValuesAsField(name, unparsed.stream()
			.map(converter == null ? Object::toString : converter)
			.toArray(String[]::new));
	}

	public final <T> EmbedCreator addValuesAsField(String name, List<String> values) {
		return addValuesAsField(name, values.toArray(String[]::new));
	}


	public EmbedCreator addLink(Message message) {

		if (message == null) return this;

		this.builder
			.addField("Message link!", "[Jump!](" + AmongUsGame.getMessageLink(message) + ")", false);

		return this;
	}

	public EmbedCreator setTitle(String name) {
		this.builder.setTitle(name);

		return this;
	}

	public EmbedCreator mentionUser(String title, User user) {

		if (user == null) return this;

		this.builder
			.addField(title, user.getAsMention(), false);

		return this;
	}

	public EmbedCreator setColor(Color color) {
		this.builder
			.setColor(color);

		return this;
	}

	public MessageEmbed create(User invoker) {

		if (invoker != null) {
			this.builder.setFooter("Invoked By " + invoker.getName(), invoker.getAvatarUrl());
		}


		return this.builder.build();
	}

	public EmbedBuilder getAsBuilder() {
		return this.builder;
	}

	public MessageEmbed create() {
		return this.builder.build();
	}

}
