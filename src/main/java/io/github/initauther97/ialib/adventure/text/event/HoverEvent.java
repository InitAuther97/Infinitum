package io.github.initauther97.ialib.adventure.text.event;

import io.github.initauther97.ialib.adventure.AdventureObject;
import io.github.initauther97.ialib.adventure.SupportedLang;
import io.github.initauther97.ialib.adventure.nbt.api.BinaryTagHolder;
import io.github.initauther97.ialib.adventure.text.PlainTextEntry;
import io.github.initauther97.ialib.adventure.text.TextEntry;
import io.github.initauther97.ialib.adventure.key.Key;

import java.util.UUID;

public record HoverEvent(String action, Object value)
        implements AdventureObject<net.kyori.adventure.text.event.HoverEvent<?>> {
    @Override
    public net.kyori.adventure.text.event.HoverEvent<?> get(SupportedLang lang, Object... args) {
        switch(action) {
            case "SHOW_TEXT":
                return net.kyori.adventure.text.event.HoverEvent.showText(((TextEntry) value).get(lang, args));
            case "SHOW_ITEM":
                return net.kyori.adventure.text.event.HoverEvent.showItem((net.kyori.adventure.text.event.HoverEvent.ShowItem) value);
            case "SHOW_ENTITY":
                return net.kyori.adventure.text.event.HoverEvent.showEntity((net.kyori.adventure.text.event.HoverEvent.ShowEntity) value);
            default:
                throw new IllegalArgumentException();
        }
    }

    public record ShowEntity(Key key, TextEntry uuid, TextEntry name) implements AdventureObject<net.kyori.adventure.text.event.HoverEvent.ShowEntity> {
        @Override
        public net.kyori.adventure.text.event.HoverEvent.ShowEntity get(SupportedLang lang, Object... args) {
            return net.kyori.adventure.text.event.HoverEvent.ShowEntity.of(
                    key.get(lang, args),
                    UUID.fromString(uuid.get(lang, args).content()),
                    name.get(lang, args)
            );
        }
    }

    public record ShowItem(Key key, TextEntry amount, BinaryTagHolder tag) implements AdventureObject<net.kyori.adventure.text.event.HoverEvent.ShowItem> {

        public ShowItem(Key key, int amount, BinaryTagHolder tag) {
            this(key, new PlainTextEntry(String.valueOf(amount)), tag);
        }

        @Override
        public net.kyori.adventure.text.event.HoverEvent.ShowItem get(SupportedLang lang, Object... args) {
            return net.kyori.adventure.text.event.HoverEvent.ShowItem.of(
                    key.get(lang, args),
                    Integer.parseInt(amount.get(lang, args).content()),
                    tag.get(lang, args)
            );
        }
    }
}
