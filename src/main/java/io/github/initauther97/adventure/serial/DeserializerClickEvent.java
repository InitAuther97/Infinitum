package io.github.initauther97.adventure.serial;

import io.github.initauther97.adventure.text.event.ClickEvent;

import java.util.Map;

public class DeserializerClickEvent implements Deserializer<Map<String, Object>, ClickEvent> {
    @Override
    public ClickEvent deserialize(Map<String, Object> value, DeserializationContext ctx) {
        return new ClickEvent(
                Deserializers.textEntry(value.get("action"), ctx),
                Deserializers.textEntry(value.get("value"), ctx)
        );
    }
}
