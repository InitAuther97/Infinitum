package io.github.initauther97.ialib.adventure.text;

import io.github.initauther97.ialib.adventure.SupportedLang;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;

public record PlainTextEntry(String value) implements TextEntry {
    public TextComponent get(SupportedLang lang, Object... args) {
        return Component.text(value);
    }
}