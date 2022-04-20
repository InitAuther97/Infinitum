package io.github.initauther97.nugget.adventure.text;

import io.github.initauther97.nugget.adventure.SupportedLang;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;

import java.util.List;

public record ComplexTextEntry(List<TextEntry> entries) implements TextEntry {
    @Override
    public TextComponent get(SupportedLang lang, Object... args) {
        TextComponent component = Component.empty();
        for(TextEntry entry : entries) {
            component = component.append(entry.get(lang, args));
        }
        return component;
    }
}
