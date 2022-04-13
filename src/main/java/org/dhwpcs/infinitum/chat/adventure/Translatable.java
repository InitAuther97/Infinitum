package org.dhwpcs.infinitum.chat.adventure;

import io.github.initauther97.nugget.adventure.SupportedLang;
import io.github.initauther97.nugget.adventure.text.TextEntry;
import net.kyori.adventure.text.TextComponent;

import java.util.function.Consumer;

public interface Translatable {
    TextComponent translate(SupportedLang lang);

    default Translatable configure(Consumer<TextComponent> configure) {
        return lang -> {
            TextComponent component = translate(lang);
            configure.accept(component);
            return component;
        };
    }

    static Translatable wrapNugget(TextEntry entry, Object... args) {
        return new NuggetAdvWrapper(entry, args);
    }

    static Translatable join(Translatable... translatables) {
        return new JoinedTranslatable(translatables);
    }

    static Translatable plain(String raw) {
        return new PlainTranslatable(raw);
    }
}
