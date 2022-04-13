package io.github.initauther97.nugget.adventure.serial;

import io.github.initauther97.nugget.adventure.text.event.HoverEvent;

import java.util.Map;

public class DeserializerHoverEvent implements Deserializer<Map<String,Object>, HoverEvent> {

    @Override
    public HoverEvent deserialize(Map<String, Object> value, DeserializationContext ctx) {
        String action = (String) value.get("action");
        Object val;

        boolean isText = value.containsKey("text");
        boolean isEntity = value.containsKey("entity");
        boolean isItem = value.containsKey("stack");
        if(isEntity && isItem || isText && isEntity || isItem && isText) {
            throw new IllegalArgumentException();
        }

        if(isText) {
            val = Deserializers.textEntry(value.get("text"), ctx);
        } else if(isEntity) {
            val = Deserializers.showEntity((Map<String, Object>) value.get("entity"), ctx);
        } else if(isItem) {
            val = Deserializers.showItem(value.get("item"), ctx);
        } else throw new IllegalArgumentException();

        return new HoverEvent(action, val);
    }
}
