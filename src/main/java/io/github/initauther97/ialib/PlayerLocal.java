package io.github.initauther97.ialib;

import io.github.initauther97.ialib.key.Key;
import org.bukkit.OfflinePlayer;

import java.util.function.Function;

public interface PlayerLocal<T> {
    Key<T> key();
    T get(OfflinePlayer player);
    void set(OfflinePlayer player, T value);
    T computeIfPresent(OfflinePlayer player, Function<T,T> map);
}
