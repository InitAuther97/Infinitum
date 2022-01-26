package org.dhwpcs.infinitum.arguments;

import io.github.initauther97.arguments.env.Characteristic;
import io.github.initauther97.arguments.env.Environment;
import org.dhwpcs.infinitum.GlobalConfig;
import org.dhwpcs.infinitum.arguments.character.BukkitServer;
import org.dhwpcs.infinitum.arguments.character.PlayerFuzzyMatching;

import java.util.HashSet;
import java.util.Set;

public class InfinitumEnvironment implements Environment {

    public static final Environment INSTANCE = new InfinitumEnvironment();

    private final Set<Characteristic<?>> allChars = new HashSet<>();

    public InfinitumEnvironment() {
        allChars.add(new BukkitServer());
        allChars.add(new PlayerFuzzyMatching(GlobalConfig.chatPlayerFuzzyMatching));
    }

    @Override
    public boolean grant(Characteristic<?> character) {
        return allChars.add(character);
    }

    @Override
    public Set<Characteristic<?>> characteristics() {
        return allChars;
    }

    @Override
    public boolean validate() {
        return false;

    }
}
