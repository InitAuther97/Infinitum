package io.github.initauther97.ialib.adventure.serial;

import io.github.initauther97.ialib.adventure.key.Key;
import io.github.initauther97.ialib.adventure.nbt.api.BinaryTagHolder;
import io.github.initauther97.ialib.adventure.text.TextEntry;
import io.github.initauther97.ialib.adventure.text.event.HoverEvent;

import java.util.HashMap;
import java.util.Map;

public class DeserializerShowItem implements Deserializer<Object, HoverEvent.ShowItem> {
    @Override
    public HoverEvent.ShowItem deserialize(Object value, DeserializationContext context) {
        if(value instanceof String) {
            return new HoverEvent.ShowItem(Deserializers.key(value, context), 1, null);
        } else if(value instanceof Map) {
            Map<String, Object> val = new HashMap<>((Map<String, Object>) value);
            Key key = (Key) val.computeIfPresent("key",
                    (k, v) -> Deserializers.key(v, context));
            TextEntry amount = (TextEntry) val.computeIfPresent("amount",
                    (k, v) -> Deserializers.textEntry(v, context));
            BinaryTagHolder tag = (BinaryTagHolder) val.computeIfPresent("tag",
                    (k,v) -> Deserializers.binaryTagHolder(v, context));
            return new HoverEvent.ShowItem(key, amount, tag);
        } else throw new IllegalArgumentException();
    }
}
