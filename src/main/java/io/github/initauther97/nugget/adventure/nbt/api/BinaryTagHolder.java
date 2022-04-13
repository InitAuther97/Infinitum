package io.github.initauther97.nugget.adventure.nbt.api;

import io.github.initauther97.nugget.adventure.AdventureObject;
import io.github.initauther97.nugget.adventure.SupportedLang;

import java.util.Map;

public record BinaryTagHolder(Object tag) implements AdventureObject<net.kyori.adventure.nbt.api.BinaryTagHolder> {
    @Override
    public net.kyori.adventure.nbt.api.BinaryTagHolder get(SupportedLang lang, Object... args) {
        if(tag instanceof String) {
            return net.kyori.adventure.nbt.api.BinaryTagHolder.of((String) tag);
        } else if(tag instanceof Map) {
            throw new UnsupportedOperationException("Parsing NBT is not supported yet.");
        }
        throw new IllegalArgumentException();
    }
}
