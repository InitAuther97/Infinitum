package org.dhwpcs.infinitum.chat.adventure;

import io.github.initauther97.nugget.adventure.SupportedLang;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;

public record JoinedTranslatable(Translatable... args) implements Translatable {

    @Override
    public TextComponent translate(SupportedLang lang) {
        TextComponent.Builder builder = Component.text();
        for(Translatable t : args) {
            builder.append(t.translate(lang));
        }
        return builder.build();
    }
}
