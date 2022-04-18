package io.github.initauther97.nugget.adventure.serial;

import io.github.initauther97.nugget.adventure.text.ComplexTextEntry;
import io.github.initauther97.nugget.adventure.text.TextEntry;
import io.github.initauther97.nugget.adventure.text.TextEntryWrapper;
import io.github.initauther97.nugget.adventure.text.event.ClickEvent;
import io.github.initauther97.nugget.adventure.text.event.HoverEvent;
import io.github.initauther97.nugget.adventure.text.format.TextColor;
import io.github.initauther97.nugget.adventure.text.format.TextDecoration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class DeserializerTextEntry<T extends TextEntry> implements Deserializer<Object, TextEntry> {
    @Override
    public TextEntry deserialize(Object va, DeserializationContext ctx) {
        if(va instanceof Map) {
            Map<String, Object> value = new HashMap<>((Map<String, Object>) va);
            HoverEvent hoverEvent = (HoverEvent) value.computeIfPresent("hover_event",
                    (k, v) -> Deserializers.hoverEvent((Map<String, Object>) v, ctx));
            ClickEvent clickEvent = (ClickEvent) value.computeIfPresent("click_event",
                    (k, v) -> Deserializers.clickEvent((Map<String, Object>) v, ctx));
            Set<TextDecoration> decoration = (Set<TextDecoration>) value.computeIfPresent("decoration",
                    (k, v) -> Deserializers.textDecoration((List<String>) v, ctx));
            TextColor color = (TextColor) value.computeIfPresent("color",
                    (k, v) -> Deserializers.textColor(v, ctx));
            TextEntry delegate;
            boolean isTranslation = value.containsKey("translate");
            boolean isArgument = value.containsKey("argument");
            boolean isComplex = value.containsKey("value");
            boolean isFormat = value.containsKey("format");
            if (btb(isTranslation) + btb(isArgument) + btb(isComplex) + btb(isFormat) != 1) {
                throw new IllegalArgumentException("Found multiple value marker in entry!");
            }

            if (isTranslation) {
                delegate = Deserializers.TRANSLATE_TEXT_ENTRY.deserializeValue(value.get("translate"), ctx);
            } else if (isArgument) {
                delegate = Deserializers.ARGUMENT_TEXT_ENTRY.deserializeValue(value.get("argument"), ctx);
            } else if (isFormat) {
                delegate = Deserializers.FORMAT_TEXT_ENTRY.deserializeValue(value.get("format"), ctx);
            } else {
                Object val = value.get("value");
                if (val instanceof List) {
                    List<Object> vals = (List<Object>) val;
                    delegate = new ComplexTextEntry(
                            vals.stream().map(v -> deserialize(v, ctx)).collect(Collectors.toList()));
                } else {
                    delegate = deserialize(val, ctx);
                }
            }
            return new TextEntryWrapper(delegate, decoration, color, hoverEvent, clickEvent);
        } else return Deserializers.PLAIN_TEXT_ENTRY.deserializeValue(va, ctx);
    }

    public abstract T deserializeValue(Object value, DeserializationContext ctx);

    static byte btb(boolean b) {
        return (byte) (b ? 1 : 0);
    }
}
