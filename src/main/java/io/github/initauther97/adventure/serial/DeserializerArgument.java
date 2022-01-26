package io.github.initauther97.adventure.serial;

import io.github.initauther97.adventure.text.ArgumentTextEntry;

public class DeserializerArgument extends DeserializerTextEntry<ArgumentTextEntry> {
    @Override
    public ArgumentTextEntry deserializeValue(Object value, DeserializationContext ctx) {
        return new ArgumentTextEntry(Integer.parseInt(value.toString()));
    }
}
