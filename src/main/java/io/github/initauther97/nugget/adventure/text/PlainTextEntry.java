package io.github.initauther97.nugget.adventure.text;

import io.github.initauther97.nugget.adventure.SupportedLang;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;

import java.util.function.Supplier;

public record PlainTextEntry(Supplier<String> value) implements TextEntry {
    public TextComponent get(SupportedLang lang, Object... args) {
        return Component.text(value.get());
    }

    public static PlainTextEntry plain(String value) {
        return new PlainTextEntry(() -> value);
    }

    public static PlainTextEntry supply(Supplier<String> value) {
        return new PlainTextEntry(value);
    }
}
