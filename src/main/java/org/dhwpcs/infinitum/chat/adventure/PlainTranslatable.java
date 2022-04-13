package org.dhwpcs.infinitum.chat.adventure;

import io.github.initauther97.nugget.adventure.SupportedLang;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;

public record PlainTranslatable(String raw) implements Translatable {
    @Override
    public TextComponent translate(SupportedLang lang) {
        return Component.text(raw);
    }
}
