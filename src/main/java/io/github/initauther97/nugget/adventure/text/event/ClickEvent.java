package io.github.initauther97.nugget.adventure.text.event;

import io.github.initauther97.nugget.adventure.AdventureObject;
import io.github.initauther97.nugget.adventure.SupportedLang;
import io.github.initauther97.nugget.adventure.text.TextEntry;

public record ClickEvent(TextEntry action, TextEntry value) implements AdventureObject<net.kyori.adventure.text.event.ClickEvent> {
    @Override
    public net.kyori.adventure.text.event.ClickEvent get(SupportedLang lang, Object... args) {
        String action = action().get(lang, args).content();
        String value = value().get(lang, args).content();
        return switch (action) {
            case "OPEN_URL" -> net.kyori.adventure.text.event.ClickEvent.openUrl(value);
            case "OPEN_FILE" -> net.kyori.adventure.text.event.ClickEvent.openFile(value);
            case "RUN_COMMAND" -> net.kyori.adventure.text.event.ClickEvent.runCommand(value);
            case "SUGGEST_COMMAND" -> net.kyori.adventure.text.event.ClickEvent.suggestCommand(value);
            case "CHANGE_PAGE" -> net.kyori.adventure.text.event.ClickEvent.changePage(value);
            case "COPY_TO_CLIPBOARD" -> net.kyori.adventure.text.event.ClickEvent.copyToClipboard(value);
            default -> null;
        };
    }
}
