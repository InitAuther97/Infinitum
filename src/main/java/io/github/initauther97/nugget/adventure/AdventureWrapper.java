package io.github.initauther97.nugget.adventure;

import io.github.initauther97.nugget.adventure.serial.DeserializationContext;
import io.github.initauther97.nugget.adventure.text.TextEntry;
import io.github.initauther97.nugget.file.FileManager;
import io.github.initauther97.nugget.file.type.FolderType;
import io.github.initauther97.nugget.file.type.RegularFileType;
import net.kyori.adventure.text.Component;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.Plugin;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.nio.file.Path;
import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

public class AdventureWrapper {

    final Map<String, TextEntry> allComponents;
    final Map<SupportedLang, Map<String, String>> i18n;
    Function<UUID, SupportedLang> langPrefs;
    Supplier<SupportedLang> consoleLangPrefs;
    final DeserializationContext context;

    public AdventureWrapper(Path root) {
        try {
            FileManager manager = new FileManager(root);
            Yaml cfg = new Yaml();
            Map<String, Object> result = cfg.load(manager.visit(RegularFileType.INPUT_READER, "root.yml"));
            FileManager langRoot = manager.visit(FolderType.MANAGER, "lang");
            i18n = new EnumMap<>(SupportedLang.class);
            for (SupportedLang lang : SupportedLang.values()) {
                i18n.put(lang, cfg.load(langRoot.visit(RegularFileType.INPUT_READER, lang.name().toLowerCase(Locale.ROOT) + ".yml")));
            }
            context = new DeserializationContext(i18n);
            allComponents = ComponentParser.parse(result, context);
            allComponents.forEach((k, v) -> System.out.println(k));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public AdventureWrapper(Plugin plugin, String rt) {
        Path root = Path.of(rt);
        Yaml cfg = new Yaml();
        Map<String, Object> result = cfg.load(plugin.getResource(root.resolve("root.yml").toString()));
        Path langRoot = root.resolve("lang");
        i18n = new EnumMap<>(SupportedLang.class);
        for (SupportedLang lang : SupportedLang.values()) {
            i18n.put(lang, cfg.load(plugin.getResource(langRoot.resolve(lang.name().toLowerCase(Locale.ROOT) + ".yml").toString())));
        }
        context = new DeserializationContext(i18n);
        allComponents = ComponentParser.parse(result, context);
    }

    public DeserializationContext getContext() {
        return context;
    }

    public void setLangPrefs(Function<UUID, SupportedLang> langPrefs, Supplier<SupportedLang> consolePrefs) {
        this.langPrefs = langPrefs;
        this.consoleLangPrefs = consolePrefs;
    }

    public Component format(String entry, CommandSender receiver, Object... args) {
        if (receiver instanceof ConsoleCommandSender) {
            return format(entry, consoleLangPrefs.get(), args);
        } else if (receiver instanceof OfflinePlayer it) {
            return format(entry, langPrefs.apply(it.getUniqueId()), args);
        } else {
            return format(entry, SupportedLang.EN_US, args);
        }
    }

    public Component format(String entry, SupportedLang lang, Object... args) {
        return allComponents.get(entry).get(lang, args);
    }

    public TextEntry get(String entry) {
        return allComponents.get(entry);
    }
}
