package io.github.initauther97.nugget.adventure.text;

import io.github.initauther97.nugget.adventure.SupportedLang;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;

import java.util.ArrayList;
import java.util.List;

public record ComplexTextEntry(List<TextEntry> entries) implements TextEntry {
    @Override
    public TextComponent get(SupportedLang lang, Object... args) {
        List<TextComponent> components = new ArrayList<>(entries.size());
        for (TextEntry entry : entries) {
            components.add(entry.get(lang, args));
        }
        return components.stream().reduce(TextComponent::append).orElse(Component.empty());
    }
}
