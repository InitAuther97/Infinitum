package org.dhwpcs.infinitum;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
import org.dhwpcs.infinitum.chat.data.MsgHistory;
import org.dhwpcs.infinitum.chunkforcer.ChunkForcer;
import org.dhwpcs.infinitum.dailictivity.Daily;
import org.dhwpcs.infinitum.http.InfinitumHttp;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class Global {

    public static boolean sandDuping;
    public static boolean fixExplosion;
    public static boolean voteOpAbsoluteDisagreement;
    public static Map<UUID, SupportedLang> langPrefs;
    public static SupportedLang consoleLang;
    public static boolean chatPlayerFuzzyMatching;
    public static InfinitumHttp server;
    public static Daily daily;
    public static IALib lib;
    private static MsgHistory history;
    private static ChunkForcer cf;
    private static PlayerLocal<Message> msgLocal;

    public static final DateTimeFormatter FORMATTER = new DateTimeFormatterBuilder()
            .appendValue(ChronoField.YEAR, 4)
            .appendLiteral('/')
            .appendValue(ChronoField.MONTH_OF_YEAR, 2)
            .appendLiteral('/')
            .appendValue(ChronoField.DAY_OF_MONTH, 2)
            .appendLiteral(' ')
            .appendValue(ChronoField.HOUR_OF_DAY, 2)
            .appendLiteral(':')
            .appendValue(ChronoField.MINUTE_OF_HOUR, 2)
            .appendLiteral(':')
            .appendValue(ChronoField.SECOND_OF_MINUTE, 2)
            .toFormatter();

    public static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .create();

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

    public static void setHttp(InfinitumHttp http) {
        Global.server = http;
    }

    public static void setDaily(Daily daily) {
        Global.daily = daily;
    }

    public static void load(ConfigurationSection config, Plugin instance, IALib lib) {
        Global.lib = lib;
        Preconditions.checkNotNull(config);
        Preconditions.checkNotNull(instance);
        try {
            read(config, instance);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        setMessageLocal(lib.createPlayerLocal(Message.class));
        I18n.initAdventure(
                instance.getDataFolder().toPath().resolve("assets/infpaper/text"),
                Global::getLanguage, Global::getConsoleLang, lib
        );
        setDaily(new Daily(instance));
        Bukkit.getPluginManager().registerEvents(daily.getListener(), instance);
    }

    public static void store(ConfigurationSection config, Plugin plugin) {
        Preconditions.checkNotNull(plugin);
        write(config);
    }

    public static SupportedLang getLanguage(UUID uid) {
        return langPrefs.getOrDefault(uid, SupportedLang.EN_US);
    }

    public static SupportedLang getConsoleLang() {
        return consoleLang;
    }

    public static void setLanguage(CommandSender sender, SupportedLang lang) {
        if (sender instanceof ConsoleCommandSender) {
            consoleLang = lang;
        } else if (sender instanceof Player) {
            langPrefs.put(((Player) sender).getUniqueId(), SupportedLang.EN_US);
        }
    }

    public static void read(ConfigurationSection config, Plugin instance) throws IOException {
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

        ConfigurationSection cs = config.getConfigurationSection("cf");
        if(cs == null) {
            cs = config.createSection("cf");
        }
        setChunkForcer(ChunkForcer.fromConfig(cs, instance));

        Global.consoleLang = SupportedLang.valueOf(config.getString("console_lang"));

        IALib lib = (IALib) Bukkit.getPluginManager().getPlugin("IALib");
        msgLocal = lib.createPlayerLocal(Message.class);

        setHttp(new InfinitumHttp(config.getInt("http_port"), instance));
    }

    public static void write(ConfigurationSection config) {
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
        ChunkForcer.saveConfig(cf, config.getConfigurationSection("modifiers"));
    }

    public static MsgHistory getHistory() {
        return history;
    }

    public static void setHistory(MsgHistory history) {
        Global.history = history;
    }
}
