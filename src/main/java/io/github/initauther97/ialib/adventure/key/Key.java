package io.github.initauther97.ialib.adventure.key;

import io.github.initauther97.ialib.adventure.AdventureObject;
import io.github.initauther97.ialib.adventure.SupportedLang;
import io.github.initauther97.ialib.adventure.text.TextEntry;

import java.util.Map;

public record Key(Object obj) implements AdventureObject<net.kyori.adventure.key.Key> {
    @Override
    public net.kyori.adventure.key.Key get(SupportedLang lang, Object... args) {
        if(obj instanceof String) {
            return net.kyori.adventure.key.Key.key((String) obj);
        } else if(obj instanceof Map) {
            Map<String, TextEntry> elements = (Map<String, TextEntry>) obj;
            return net.kyori.adventure.key.Key.key(
                    elements.get("namespace").get(lang, args).content(),
                    elements.get("id").get(lang, args).content()
            );
        } else throw new IllegalArgumentException();
    }
}
