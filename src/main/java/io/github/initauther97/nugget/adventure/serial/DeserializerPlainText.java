package io.github.initauther97.nugget.adventure.serial;

import io.github.initauther97.nugget.adventure.ComponentParser;
import io.github.initauther97.nugget.adventure.text.PlainTextEntry;

public class DeserializerPlainText extends DeserializerTextEntry<PlainTextEntry> {
    @Override
    public PlainTextEntry deserializeValue(Object value, DeserializationContext ctx) {
        return ComponentParser.plain(value.toString());
    }
}
