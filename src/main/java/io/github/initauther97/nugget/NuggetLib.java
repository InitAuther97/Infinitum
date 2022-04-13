package io.github.initauther97.nugget;

import io.github.initauther97.nugget.adventure.AdventureWrapper;
import io.github.initauther97.nugget.local.PlayerLocal;

import java.nio.file.Path;


public class NuggetLib {

    public static boolean checkPreload() {
        return true;
    }

    public<T> PlayerLocal<T> createPlayerLocal(Class<T> type) {
        return new PlayerLocal<>(type);
    }

    public AdventureWrapper createAdventureWrapper(Path root) {
        return new AdventureWrapper(root);
    }
}
