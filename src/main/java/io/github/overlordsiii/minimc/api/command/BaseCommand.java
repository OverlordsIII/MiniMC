package io.github.overlordsiii.minimc.api.command;

import java.util.function.Predicate;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.GenericEvent;

public interface BaseCommand<T extends GenericEvent> {

	void execute(T event);

	default Predicate<T> getPredicate() {
		return event -> true;
	}

	default Permission[] getNeededPermissions() {
		return null;
	}

}
