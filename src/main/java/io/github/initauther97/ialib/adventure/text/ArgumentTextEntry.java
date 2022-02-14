package io.github.initauther97.ialib.adventure.text;

import io.github.initauther97.ialib.adventure.AdventureObject;
import io.github.initauther97.ialib.adventure.SupportedLang;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;

public record ArgumentTextEntry(int pointer) implements TextEntry {

    @Override
    public TextComponent get(SupportedLang lang, Object... args) {
        if(args[pointer] instanceof AdventureObject) {
            AdventureObject<?> obj = (AdventureObject<?>) args[pointer];
            if(obj instanceof TextEntry) {
                return ((TextEntry) obj).get(lang, args);
            } else {
                throw new IllegalArgumentException();
            }
        } else if(args[pointer] instanceof String) {
            return Component.text((String) args[pointer]);
        } else return Component.text(args[pointer].toString());
    }
}
