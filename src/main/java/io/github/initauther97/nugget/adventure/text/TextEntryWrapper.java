package io.github.initauther97.nugget.adventure.text;

import io.github.initauther97.nugget.adventure.SupportedLang;
import io.github.initauther97.nugget.adventure.text.event.ClickEvent;
import io.github.initauther97.nugget.adventure.text.event.HoverEvent;
import io.github.initauther97.nugget.adventure.text.format.TextColor;
import io.github.initauther97.nugget.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.TextComponent;

import java.util.Set;

public record TextEntryWrapper(TextEntry delegate, Set<TextDecoration> decorations, TextColor color,
                               HoverEvent hoverEvent, ClickEvent clickEvent) implements TextEntry {
    @Override
    public TextComponent get(SupportedLang lang, Object... args) {
        TextComponent result = delegate.get(lang, args);
        if (hoverEvent != null) {
            result.hoverEvent(hoverEvent.get(lang, args));
        }
        if (clickEvent != null) {
            result.clickEvent(clickEvent.get(lang, args));
        }
        if (color != null) {
            result.color(color.get(lang, args));
        }
        if (decorations != null) {
            for (TextDecoration decoration : decorations) {
                result.decoration(decoration.get(lang, args), net.kyori.adventure.text.format.TextDecoration.State.TRUE);
            }
        }
        return result;
    }
}
