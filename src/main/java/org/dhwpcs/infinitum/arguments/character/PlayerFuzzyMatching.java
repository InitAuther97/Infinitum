package org.dhwpcs.infinitum.arguments.character;

import io.github.initauther97.arguments.env.Characteristic;

public record PlayerFuzzyMatching(boolean value) implements Characteristic<Boolean> {
    @Override
    public String identifier() {
        return "player_fuzzy_matching";
    }

    @Override
    public Boolean get() {
        return value;
    }
}
