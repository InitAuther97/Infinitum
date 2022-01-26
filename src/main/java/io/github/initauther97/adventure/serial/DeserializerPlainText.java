package io.github.initauther97.adventure.serial;

import io.github.initauther97.adventure.text.PlainTextEntry;

public class DeserializerPlainText extends DeserializerTextEntry<PlainTextEntry> {
    @Override
    public PlainTextEntry deserializeValue(Object value, DeserializationContext ctx) {
        return new PlainTextEntry(value.toString());
    }
}
