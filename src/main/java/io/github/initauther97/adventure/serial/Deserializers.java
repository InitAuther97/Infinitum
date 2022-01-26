package io.github.initauther97.adventure.serial;

import io.github.initauther97.adventure.key.Key;
import io.github.initauther97.adventure.nbt.api.BinaryTagHolder;
import io.github.initauther97.adventure.text.*;
import io.github.initauther97.adventure.text.event.ClickEvent;
import io.github.initauther97.adventure.text.event.HoverEvent;
import io.github.initauther97.adventure.text.format.TextColor;
import io.github.initauther97.adventure.text.format.TextDecoration;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Deserializers {
    public static final Deserializer<Map<String, Object>, HoverEvent> HOVER_EVENT = new DeserializerHoverEvent();
    public static final Deserializer<Map<String, Object>, ClickEvent> CLICK_EVENT = new DeserializerClickEvent();
    public static final Deserializer<Object, TextColor> TEXT_COLOR = Deserializer.function(TextColor::new);
    public static final Deserializer<Object, Key> KEY = new DeserializerKey();
    public static final Deserializer<Map<String, Object>, HoverEvent.ShowEntity> HOVER_SHOW_ENTITY = new DeserializerShowEntity();
    public static final Deserializer<Object, HoverEvent.ShowItem> HOVER_SHOW_ITEM = new DeserializerShowItem();
    public static final Deserializer<Object, BinaryTagHolder> BINARY_TAG_HOLDER = Deserializer.function(BinaryTagHolder::new);
    public static final Deserializer<String, TextDecoration> TEXT_DECORATION = Deserializer.complexFunction(PlainTextEntry::new, TextDecoration::new);

    public static final DeserializerTextEntry<FormatTextEntry> FORMAT_TEXT_ENTRY = new DeserializerFormatText();
    public static final DeserializerTextEntry<PlainTextEntry> PLAIN_TEXT_ENTRY = new DeserializerPlainText();
    public static final DeserializerTextEntry<TranslateTextEntry> TRANSLATE_TEXT_ENTRY = new DeserializerTranslation();
    public static final DeserializerTextEntry<ArgumentTextEntry> ARGUMENT_TEXT_ENTRY = new DeserializerArgument();

    public static HoverEvent hoverEvent(Map<String, Object> hoverEvent, DeserializationContext ctx) {
        return HOVER_EVENT.deserialize(hoverEvent, ctx);
    }

    public static ClickEvent clickEvent(Map<String, Object> clickEvent, DeserializationContext ctx) {
        return CLICK_EVENT.deserialize(clickEvent, ctx);
    }

    public static Set<TextDecoration> textDecoration(List<String> textDecoration, DeserializationContext ctx) {
        Set<TextDecoration> result = new HashSet<>(textDecoration.size());
        for(String str : textDecoration) {
            result.add(TEXT_DECORATION.deserialize(str, ctx));
        }
        return result;
    }

    public static TextColor textColor(Object color, DeserializationContext ctx) {
        return TEXT_COLOR.deserialize(color, ctx);
    }

    public static TextEntry textEntry(Object text, DeserializationContext ctx) {
        return PLAIN_TEXT_ENTRY.deserialize(text, ctx);
    }

    public static PlainTextEntry plainText(String text, DeserializationContext ctx) {
        return PLAIN_TEXT_ENTRY.deserializeValue(text, ctx);
    }

    public static TranslateTextEntry translateText(String entry, DeserializationContext ctx) {
        return TRANSLATE_TEXT_ENTRY.deserializeValue(entry, ctx);
    }

    public static Key key(Object key, DeserializationContext ctx) {
        return KEY.deserialize(key, ctx);
    }

    public static HoverEvent.ShowEntity showEntity(Map<String, Object> entity, DeserializationContext ctx) {
        return HOVER_SHOW_ENTITY.deserialize(entity, ctx);
    }

    public static HoverEvent.ShowItem showItem(Object item, DeserializationContext ctx) {
        return HOVER_SHOW_ITEM.deserialize(item, ctx);
    }

    public static BinaryTagHolder binaryTagHolder(Object tag, DeserializationContext ctx) {
        return BINARY_TAG_HOLDER.deserialize(tag, ctx);
    }
}
