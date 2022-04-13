package io.github.initauther97.nugget.adventure.serial;

import io.github.initauther97.nugget.adventure.SupportedLang;

import java.util.Map;

public class DeserializationContext {
    private final Map<SupportedLang, Map<String, String>> i18n;

    public DeserializationContext(Map<SupportedLang, Map<String, String>> i18n) {
        this.i18n = i18n;
    }

    public String localize(SupportedLang lang, String id) {
        return i18n.get(lang).get(id);
    }
}
