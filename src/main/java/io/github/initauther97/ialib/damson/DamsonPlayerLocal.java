package io.github.initauther97.ialib.damson;

import com.google.common.reflect.TypeToken;
import io.github.initauther97.ialib.PlayerLocal;
import io.github.initauther97.ialib.damson.internal.Locals;
import io.github.initauther97.ialib.key.Key;
import org.bukkit.OfflinePlayer;

import java.util.function.Function;

public class DamsonPlayerLocal<T> implements PlayerLocal<T> {

    private final Key<T> key;

    public DamsonPlayerLocal(Class<T> type) {
        key = Key.make(type);
        Locals.allocate(key);
    }

    public DamsonPlayerLocal() {
        this((Class<T>)(new TypeToken<T>(){}).getType());
    }

    @Override
    public Key<T> key() {
        return this.key;
    }

    @Override
    public T get(OfflinePlayer player) {
        return (T) Locals.get(key, player.getUniqueId());
    }

    @Override
    public void set(OfflinePlayer player, T value) {
        Locals.put(key, player.getUniqueId(), value);
    }

    @Override
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
