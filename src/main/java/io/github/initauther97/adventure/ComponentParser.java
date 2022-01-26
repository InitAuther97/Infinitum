package io.github.initauther97.adventure;

import io.github.initauther97.adventure.serial.DeserializationContext;
import io.github.initauther97.adventure.serial.Deserializers;
import net.kyori.adventure.text.Component;

import java.util.HashMap;
import java.util.Map;

public class ComponentParser {
    static Map<String, AdventureObject<? extends Component>> parse(Map<String, Object> entries, DeserializationContext ctx) {
        Map<String, AdventureObject<? extends Component>> result = new HashMap<>(entries.size());
        entries.entrySet().forEach(entry -> result.put(entry.getKey(), Deserializers.PLAIN_TEXT_ENTRY.deserialize(entry.getValue(), ctx)));
        return result;
    }
}
