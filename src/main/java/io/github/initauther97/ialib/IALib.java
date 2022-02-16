package io.github.initauther97.ialib;

import io.github.initauther97.ialib.adventure.AdventureWrapper;
import io.github.initauther97.ialib.damson.DamsonPlayerLocal;

import java.nio.file.Path;


public class IALib {

    public static boolean checkPreload() {
        return true;
    }

    public<T> PlayerLocal<T> createPlayerLocal(Class<T> type) {
        return new DamsonPlayerLocal<>(type);
    }

    public AdventureWrapper createAdventureWrapper(Path root) {
        return new AdventureWrapper(root);
    }
}
