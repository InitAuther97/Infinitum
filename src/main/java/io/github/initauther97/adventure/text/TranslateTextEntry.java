package io.github.initauther97.adventure.text;

import io.github.initauther97.adventure.SupportedLang;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;

import java.util.function.Function;

public record TranslateTextEntry(
        Function<SupportedLang, String> formatted) implements TextEntry {

    @Override
    public TextComponent get(SupportedLang lang, Object... args) {
        return Component.text(formatted.apply(lang));
    }
}
