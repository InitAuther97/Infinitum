package io.github.initauther97.nugget.adventure.serial;

import io.github.initauther97.nugget.adventure.ComponentParser;
import io.github.initauther97.nugget.adventure.text.ArgumentTextEntry;

public class DeserializerArgument extends DeserializerTextEntry<ArgumentTextEntry> {
    @Override
    public ArgumentTextEntry deserializeValue(Object value, DeserializationContext ctx) {
        return ComponentParser.argument(Integer.parseInt(value.toString()));
    }
}
