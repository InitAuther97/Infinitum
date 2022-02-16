package org.dhwpcs.infinitum;

import io.github.initauther97.ialib.IALib;
import io.github.initauther97.ialib.adventure.AdventureObject;
import io.github.initauther97.ialib.adventure.AdventureWrapper;
import io.github.initauther97.ialib.adventure.ComponentParser;
import io.github.initauther97.ialib.adventure.SupportedLang;
import io.github.initauther97.ialib.adventure.text.TranslateTextEntry;
import net.kyori.adventure.text.Component;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;

import java.nio.file.Path;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class I18n {

    public static AdventureWrapper TEXTS;
    public static final Component INF_PREFIX = Component.text("[DHW INF]");

    public static void initAdventure(Path root, Function<UUID, SupportedLang> playerLang, Supplier<SupportedLang> consoleLang, IALib lib) {
        TEXTS = lib.createAdventureWrapper(root);
        TEXTS.setLangPrefs(playerLang, consoleLang);
    }

    public static Component format(String entry, CommandSender receiver, Object... args) {
        return TEXTS.format(entry, receiver, args);
    }

    public static Component format(String entry, SupportedLang lang, Object... args) {
        return TEXTS.format(entry, lang, args);
    }

    public static AdventureObject<? extends Component> get(String entry) {
        return TEXTS.get(entry);
    }

    public static TranslateTextEntry translate(String name) {
        return ComponentParser.translate(name, TEXTS);
    }

    public static void broadcast(Server s, String entry, Object... args) {
        s.getOnlinePlayers().forEach(p -> p.sendMessage(INF_PREFIX.append(TEXTS.format(entry, p, args))));
    }

    public static void sendMessage(String entry, CommandSender receiver, Object... args) {
        receiver.sendMessage(INF_PREFIX.append(TEXTS.format(entry, receiver, args)));
    }
}
