package io.github.initauther97.adventure.serial;

import io.github.initauther97.adventure.text.TranslateTextEntry;

public class DeserializerTranslation extends DeserializerTextEntry<TranslateTextEntry> {

    @Override
    public TranslateTextEntry deserializeValue(Object value, DeserializationContext ctx) {
        return new TranslateTextEntry(lang -> ctx.localize(lang, value.toString()));
    }
}
