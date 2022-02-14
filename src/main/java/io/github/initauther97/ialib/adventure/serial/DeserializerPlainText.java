package io.github.initauther97.ialib.adventure.serial;

import io.github.initauther97.ialib.adventure.ComponentParser;
import io.github.initauther97.ialib.adventure.text.PlainTextEntry;

public class DeserializerPlainText extends DeserializerTextEntry<PlainTextEntry> {
    @Override
    public PlainTextEntry deserializeValue(Object value, DeserializationContext ctx) {
        return ComponentParser.plain(value.toString());
    }
}
