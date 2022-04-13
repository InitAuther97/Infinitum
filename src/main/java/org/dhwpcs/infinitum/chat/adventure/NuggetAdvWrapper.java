package org.dhwpcs.infinitum.chat.adventure;

import io.github.initauther97.nugget.adventure.SupportedLang;
import io.github.initauther97.nugget.adventure.text.TextEntry;
import net.kyori.adventure.text.TextComponent;

public record NuggetAdvWrapper(TextEntry entry, Object[] arguments) implements Translatable {
    @Override
    public TextComponent translate(SupportedLang lang) {
        return entry.get(lang, arguments);
    }
}
