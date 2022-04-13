package io.github.initauther97.nugget.adventure.serial;

import io.github.initauther97.nugget.adventure.key.Key;

import java.util.Map;

public class DeserializerKey implements Deserializer<Object, Key> {
    @Override
    public Key deserialize(Object value, DeserializationContext context) {
        if(value instanceof String) {
            return new Key(value);
        } else if(value instanceof Map) {
            Map<String, Object> handle = Map.copyOf((Map<String, ?>) value);
            handle.compute("namespace", (id, v) -> Deserializers.textEntry(v, context));
            handle.compute("id", (id, v) -> Deserializers.textEntry(v, context));
            return new Key(handle);
        } else throw new IllegalArgumentException();
    }
}
