package org.dhwpcs.infinitum.i18n;

import io.github.initauther97.nugget.NuggetLib;
import io.github.initauther97.nugget.adventure.AdventureWrapper;
import io.github.initauther97.nugget.adventure.ComponentParser;
import io.github.initauther97.nugget.adventure.SupportedLang;
import io.github.initauther97.nugget.adventure.text.TextEntry;
import io.github.initauther97.nugget.adventure.text.TranslateTextEntry;
import net.kyori.adventure.text.Component;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class I18n {

    public AdventureWrapper TEXTS;
    public final Component INF_PREFIX = Component.text("[DHW INF]");
    private Map<UUID, SupportedLang> langs;
    private SupportedLang console_lang;
    private boolean initialized = false;

    public Component format(String entry, CommandSender receiver, Object... args) {
        return TEXTS.format(entry, receiver, args);
    }

    public Component format(String entry, SupportedLang lang, Object... args) {
        return TEXTS.format(entry, lang, args);
    }

    public TextEntry get(String entry) {
        return TEXTS.get(entry);
    }

    public TranslateTextEntry translate(String name) {
        return ComponentParser.translate(name, TEXTS);
    }

    public void broadcast(Server s, String entry, Object... args) {
        s.getOnlinePlayers().forEach(p -> p.sendMessage(INF_PREFIX.append(TEXTS.format(entry, p, args))));
    }

    public void sendMessage(String entry, CommandSender receiver, Object... args) {
        receiver.sendMessage(INF_PREFIX.append(TEXTS.format(entry, receiver, args)));
    }

    public boolean setLanguage(CommandSender sender, SupportedLang lang) {
        if (sender instanceof ConsoleCommandSender) {
            console_lang = lang;
        } else if (sender instanceof Player p) {
            langs.put(p.getUniqueId(), SupportedLang.EN_US);
        } else {
            return false;
        }
        return true;
    }

    public SupportedLang getLanguage(CommandSender sender) {
        if (sender instanceof ConsoleCommandSender) {
            return console_lang;
        } else if (sender instanceof Player p) {
            return langs.get(p.getUniqueId());
        }
        return null;
    }

    public Iterator<Map.Entry<UUID, SupportedLang>> getLanguages() {
        return langs.entrySet().iterator();
    }

    public void setConsoleLang(SupportedLang language) {
        console_lang = language;
    }

    public void initialize(Path root, ConfigurationSection i18n, NuggetLib lib) {
        if(initialized){
            return;
        }
        initialized = true;
        TEXTS = lib.createAdventureWrapper(root);
        ConfigurationSection section = i18n.getConfigurationSection("players");
        langs = new HashMap<>();
        for(String key : section.getKeys(false)) {
            String value = section.getString(key);
            langs.put(UUID.fromString(key), SupportedLang.getOrDefault(value, SupportedLang.EN_US));
        }
        console_lang = SupportedLang.getOrDefault(section.getString("console"), SupportedLang.EN_US);
        TEXTS.setLangPrefs(langs::get, ()->console_lang);
    }
}
