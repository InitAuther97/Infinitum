package io.github.initauther97.nugget.adventure;

import io.github.initauther97.nugget.adventure.serial.DeserializationContext;
import io.github.initauther97.nugget.adventure.serial.Deserializers;
import io.github.initauther97.nugget.adventure.text.*;
import net.kyori.adventure.text.Component;

import java.util.*;

public class ComponentParser {

    static Map<String, TextEntry> parse(Map<String, Object> entries, DeserializationContext ctx) {
        Map<String, TextEntry> result = new HashMap<>(entries.size());
        entries.entrySet().forEach(entry -> result.put(entry.getKey(), Deserializers.PLAIN_TEXT_ENTRY.deserialize(entry.getValue(), ctx)));
        return result;
    }

    public static TranslateTextEntry translate(String name, AdventureWrapper adv) {
        return new TranslateTextEntry(lang -> adv.i18n.get(lang).get(name));
    }

    public static FormatTextEntry format(TextEntry raw, TextEntry... arguments) {
        return new FormatTextEntry(raw, Arrays.asList(arguments));
    }

    public static FormatTextEntry format(TextEntry raw, Collection<TextEntry> arguments) {
        return new FormatTextEntry(raw, List.copyOf(arguments));
    }

    public static FormatTextEntry format(String raw, TextEntry... arguments) {
        return new FormatTextEntry(new PlainTextEntry(() -> raw), Arrays.asList(arguments));
    }

    public static FormatTextEntry format(String raw, Collection<TextEntry> arguments) {
        return new FormatTextEntry(new PlainTextEntry(() -> raw), List.copyOf(arguments));
    }

    public static PlainTextEntry plain(String value) {
        return new PlainTextEntry(() -> value);
    }

    public static ArgumentTextEntry argument(int ptr) {
        return new ArgumentTextEntry(ptr);
    }
}
