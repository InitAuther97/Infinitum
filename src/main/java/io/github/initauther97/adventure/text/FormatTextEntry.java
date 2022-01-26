package io.github.initauther97.adventure.text;

import io.github.initauther97.adventure.SupportedLang;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;

import java.util.List;

public record FormatTextEntry(TextEntry raw, List<TextEntry> entries) implements TextEntry {
    @Override
    public TextComponent get(SupportedLang lang, Object... args) {
        String[] format = entries.stream().map(e -> e.get(lang, args).content()).toArray(String[]::new);
        return Component.text(String.format(raw.get(lang, args).content(), (Object[]) format));
    }
}
