package io.github.initauther97.nugget.adventure.text;

import io.github.initauther97.nugget.adventure.SupportedLang;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;

import java.util.List;

public record ComplexTextEntry(List<TextEntry> entries) implements TextEntry {
    @Override
    public TextComponent get(SupportedLang lang, Object... args) {
        Component[] components = new Component[entries.size()];
        for(int i = 0;i < components.length;i++) {
            components[i] = entries.get(i).get(lang, args);
        }
        return Component.text().append(components).build();
    }
}
