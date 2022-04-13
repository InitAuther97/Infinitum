package io.github.initauther97.nugget.local;

import com.google.common.reflect.TypeToken;
import io.github.initauther97.nugget.local.internal.Locals;
import io.github.initauther97.nugget.key.Key;
import org.bukkit.OfflinePlayer;

import java.util.function.Function;

public class PlayerLocal<T> {

    private final Key<T> key;

    public PlayerLocal(Class<T> type) {
        key = Key.make(type);
        Locals.allocate(key);
    }

    public PlayerLocal() {
        this((Class<T>)(new TypeToken<T>(){}).getType());
    }

    public Key<T> key() {
        return this.key;
    }

    public T get(OfflinePlayer player) {
        return (T) Locals.get(key, player.getUniqueId());
    }

    public void set(OfflinePlayer player, T value) {
        Locals.put(key, player.getUniqueId(), value);
    }

    public T computeIfPresent(OfflinePlayer player, Function<T, T> map) {
        T t = (T) Locals.get(key, player.getUniqueId());
        if(t == null) {
            return null;
        }
        t = map.apply(t);
        Locals.put(key, player.getUniqueId(), t);
        return t;
    }
}
