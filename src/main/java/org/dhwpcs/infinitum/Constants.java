package org.dhwpcs.infinitum;

import io.github.initauther97.adventure.AdventureWrapped;
import io.github.initauther97.adventure.SupportedLang;

import java.nio.file.Path;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class Constants {

    public static AdventureWrapped TEXTS;

    public static void initAdventure(Path root, Function<UUID, SupportedLang> playerLang, Supplier<SupportedLang> consoleLang) {
        TEXTS = new AdventureWrapped(root);
        TEXTS.setLangPrefs(playerLang, consoleLang);
    }

    int DEFAULT_PERIOD = 10;

    public static String getVersion() {
        return "1.0";
    }
}
