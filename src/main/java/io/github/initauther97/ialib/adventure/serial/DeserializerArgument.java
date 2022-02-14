package io.github.initauther97.ialib.adventure.serial;

import io.github.initauther97.ialib.adventure.ComponentParser;
import io.github.initauther97.ialib.adventure.text.ArgumentTextEntry;

public class DeserializerArgument extends DeserializerTextEntry<ArgumentTextEntry> {
    @Override
    public ArgumentTextEntry deserializeValue(Object value, DeserializationContext ctx) {
        return ComponentParser.argument(Integer.parseInt(value.toString()));
    }
}
