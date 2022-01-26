package io.github.initauther97.adventure.serial;

import io.github.initauther97.adventure.text.event.HoverEvent;

import java.util.Map;

public class DeserializerShowEntity implements Deserializer<Map<String, Object>, HoverEvent.ShowEntity> {
    @Override
    public HoverEvent.ShowEntity deserialize(Map<String, Object> value, DeserializationContext ctx) {
        return new HoverEvent.ShowEntity(
                Deserializers.key(value.get("id"), ctx),
                Deserializers.textEntry(value.get("uuid"), ctx),
                value.containsKey("name") ? Deserializers.textEntry(value.get("name"), ctx) : null
        );
    }
}
