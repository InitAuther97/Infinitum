package org.dhwpcs.infinitum;

import io.github.initauther97.adventure.SupportedLang;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GlobalConfig {
    public static boolean sandDuping;
    public static boolean fixExplosion;
    public static boolean voteOpAbsoluteDisagreement;
    public static Map<UUID, SupportedLang> langPrefs;
    public static SupportedLang consoleLang;
    public static boolean chatPlayerFuzzyMatching;

    public static SupportedLang getLanguage(CommandSender sender) {
        if (sender instanceof ConsoleCommandSender) {
            return consoleLang;
        } else if (sender instanceof Player) {
            return getLanguage(((Player) sender).getUniqueId());
        } else {
            return SupportedLang.EN_US;
        }
    }

    public static SupportedLang getLanguage(UUID uid) {
        return langPrefs.getOrDefault(uid, SupportedLang.EN_US);
    }

    public static SupportedLang getConsoleLang() {
        return consoleLang;
    }

    public static void setConsoleLang(SupportedLang lang) {
        consoleLang = lang;
    }

    public static void setLanguage(CommandSender sender, SupportedLang lang) {
        if (sender instanceof ConsoleCommandSender) {
            consoleLang = lang;
        } else if (sender instanceof Player) {
            langPrefs.put(((Player) sender).getUniqueId(), SupportedLang.EN_US);
        }
    }

    public static void read(FileConfiguration config) {
        ConfigurationSection modifiers = config.getConfigurationSection("modifiers");
        GlobalConfig.fixExplosion = modifiers.getBoolean("fix_explosion", false);
        GlobalConfig.sandDuping = modifiers.getBoolean("sand_duping", false);
        GlobalConfig.voteOpAbsoluteDisagreement = config.getBoolean("vote_op_absolute_disagreement", true);
        GlobalConfig.chatPlayerFuzzyMatching = config.getBoolean("chat_player_fuzzy_matching");

        GlobalConfig.langPrefs = new HashMap<>();
        ConfigurationSection langPrefs = config.getConfigurationSection("language_prefs");
        if (langPrefs == null) {
            langPrefs = config.createSection("language_prefs");
        }
        for (String key : langPrefs.getKeys(false)) {
            GlobalConfig.langPrefs.put(UUID.fromString(key), SupportedLang.valueOf(langPrefs.getString(key)));
        }

        GlobalConfig.consoleLang = SupportedLang.valueOf(config.getString("console_lang"));
    }

    public static void write(FileConfiguration config) {
        ConfigurationSection modifiers = config.getConfigurationSection("modifiers");
        modifiers.set("fix_explosion", GlobalConfig.fixExplosion);
        modifiers.set("sand_duping", GlobalConfig.sandDuping);
        config.set("vote_op_absolute_disagreement", GlobalConfig.voteOpAbsoluteDisagreement);
        config.set("chat_player_fuzzy_matching", GlobalConfig.chatPlayerFuzzyMatching);
        config.set("console_lang", GlobalConfig.consoleLang.name());
        ConfigurationSection langPrefs = config.getConfigurationSection("language_prefs");
        for(UUID key : GlobalConfig.langPrefs.keySet()) {
            langPrefs.set(key.toString(), GlobalConfig.getLanguage(key).name());
        }
    }
}
