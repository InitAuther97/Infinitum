package io.github.initauther97.ialib.adventure.text.format;

import io.github.initauther97.ialib.adventure.AdventureObject;
import io.github.initauther97.ialib.adventure.SupportedLang;
import io.github.initauther97.ialib.adventure.text.TextEntry;

public record TextDecoration(TextEntry decoration) implements AdventureObject<net.kyori.adventure.text.format.TextDecoration> {
    @Override
    public net.kyori.adventure.text.format.TextDecoration get(SupportedLang lang, Object... args) {
        return net.kyori.adventure.text.format.TextDecoration.valueOf(decoration.get(lang, args).content());
    }
}
