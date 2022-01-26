package io.github.initauther97.adventure.text;

import io.github.initauther97.adventure.SupportedLang;
import net.kyori.adventure.text.TextComponent;

import java.util.List;

public record ComplexTextEntry(List<TextEntry> entries) implements TextEntry {
    @Override
    public TextComponent get(SupportedLang lang, Object... args) {
        return TextComponent.ofChildren(entries.stream().map(e -> e.get(lang, args)).toArray(TextComponent[]::new));
    }
}
