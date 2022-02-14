package org.dhwpcs.infinitum;

import com.google.common.base.Preconditions;
import io.github.initauther97.ialib.adventure.SupportedLang;
import io.github.initauther97.ialib.IALib;
import io.github.initauther97.ialib.PlayerLocal;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.dhwpcs.infinitum.chat.data.Message;
import org.dhwpcs.infinitum.chunkforcer.ChunkForcer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Global {
    public static boolean sandDuping;
    public static boolean fixExplosion;
    public static boolean voteOpAbsoluteDisagreement;
    public static Map<UUID, SupportedLang> langPrefs;
    public static SupportedLang consoleLang;
    public static boolean chatPlayerFuzzyMatching;
    private static ChunkForcer cf;
    private static PlayerLocal<Message> msgLocal;
    public static ChunkForcer getChunkForcer() {
        return cf;
    }

    public static PlayerLocal<Message> getMessageLocal() {
        return msgLocal;
    }

    public static void setChunkForcer(ChunkForcer cf) {
        Global.cf = cf;
    }

    public static void setMessageLocal(PlayerLocal<Message> local) {
        Global.msgLocal = local;
    }

    public static void load(ConfigurationSection config, Plugin instance) {
        Preconditions.checkNotNull(config);
        Preconditions.checkNotNull(instance);
        setChunkForcer(ChunkForcer.fromConfig(config.getConfigurationSection("cf"), instance));

        IALib lib = (IALib) instance.getServer().getPluginManager().getPlugin("IALib");
        setMessageLocal(lib.createPlayerLocal(Message.class));
    }

    public static void store(ConfigurationSection config, Plugin plugin) {
        Preconditions.checkNotNull(plugin);
        ChunkForcer.saveConfig(cf, config);
    }

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

    public static boolean isSandDuping() {
        return sandDuping;
    }

    public static boolean isFixExplosion() {
        return fixExplosion;
    }

    public static boolean isVoteOpAbsoluteDisagreement() {
        return voteOpAbsoluteDisagreement;
    }

    public static Map<UUID, SupportedLang> getLangPrefs() {
        return langPrefs;
    }

    public static boolean isChatPlayerFuzzyMatching() {
        return chatPlayerFuzzyMatching;
    }

    public static void read(FileConfiguration config) {
        ConfigurationSection modifiers = config.getConfigurationSection("modifiers");
        Global.fixExplosion = modifiers.getBoolean("fix_explosion", false);
        Global.sandDuping = modifiers.getBoolean("sand_duping", false);
        Global.voteOpAbsoluteDisagreement = config.getBoolean("vote_op_absolute_disagreement", true);
        Global.chatPlayerFuzzyMatching = config.getBoolean("chat_player_fuzzy_matching");

        Global.langPrefs = new HashMap<>();
        ConfigurationSection langPrefs = config.getConfigurationSection("language_prefs");
        if (langPrefs == null) {
            langPrefs = config.createSection("language_prefs");
        }
        for (String key : langPrefs.getKeys(false)) {
            Global.langPrefs.put(UUID.fromString(key), SupportedLang.valueOf(langPrefs.getString(key)));
        }

        Global.consoleLang = SupportedLang.valueOf(config.getString("console_lang"));
        IALib lib = (IALib) Bukkit.getPluginManager().getPlugin("IALib");
    }

    public static void write(FileConfiguration config) {
        ConfigurationSection modifiers = config.getConfigurationSection("modifiers");
        modifiers.set("fix_explosion", Global.fixExplosion);
        modifiers.set("sand_duping", Global.sandDuping);
        config.set("vote_op_absolute_disagreement", Global.voteOpAbsoluteDisagreement);
        config.set("chat_player_fuzzy_matching", Global.chatPlayerFuzzyMatching);
        config.set("console_lang", Global.consoleLang.name());
        ConfigurationSection langPrefs = config.getConfigurationSection("language_prefs");
        for(UUID key : Global.langPrefs.keySet()) {
            langPrefs.set(key.toString(), Global.getLanguage(key).name());
        }
    }

}
