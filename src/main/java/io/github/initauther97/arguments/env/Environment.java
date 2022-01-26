package io.github.initauther97.arguments.env;

import io.github.initauther97.arguments.ParseErrorHandler;

import java.util.Collections;
import java.util.Set;

public interface Environment {

    Environment DEFAULT = new Environment() {
        @Override
        public Set<Characteristic<?>> characteristics() {
            return Collections.emptySet();
        }

        @Override
        public boolean grant(Characteristic<?> character) {
            return false;
        }

        @Override
        public boolean validate() {
            return true;
        }
    };

    Set<Characteristic<?>> characteristics();

    default boolean grant(Characteristic<?> character) {
        return false;
    }

    boolean validate();

    default ParseErrorHandler getErrorHandler() {
        return ParseErrorHandler.DEFAULT;
    }

    default Environment inheritFrom() {
        return DEFAULT;
    }

    default boolean fullCompatible(Environment env) {
        return characteristics().containsAll(env.characteristics()) || inheritFrom().fullCompatible(env);
    }

    default boolean has(Characteristic<?> character) {
        return characteristics().contains(character) || inheritFrom().has(character);
    }

    default boolean has(String character) {
        return characteristics().stream().anyMatch(c -> c.identifier().contentEquals(character))
                || inheritFrom().has(character);
    }

    default Characteristic<?> get(String id) {
        return has(id) ? characteristics().stream().filter(c -> c.identifier().contentEquals(id)).findFirst().get()
                : inheritFrom().get(id);
    }
}
