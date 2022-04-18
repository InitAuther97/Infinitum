package org.dhwpcs.infinitum.config;

import org.bukkit.configuration.ConfigurationSection;
import org.dhwpcs.infinitum.Infinitum;

import java.io.IOException;
import java.util.Map;
import java.util.function.Consumer;

public class MixinConfig {
    public static final String[] ENTRIES = {"mixin.sand_duping", "mixin.fix_explosion"};
    public static final Map<String, Consumer<String>> APPLIERS
            = Map.of("mixin.sand_duping", arg -> sandDuping = Boolean.parseBoolean(arg),
            "mixin.fix_explosion", arg -> fixExplosion = Boolean.parseBoolean(arg));
    public static boolean sandDuping;
    public static boolean fixExplosion;

    public static void read(ConfigurationSection modifiers) throws IOException {
        fixExplosion = modifiers.getBoolean("fix_explosion", false);
        sandDuping = modifiers.getBoolean("sand_duping", false);
    }

    public static void save(ConfigurationSection section) {
        section.set("fix_explosion", fixExplosion);
        section.set("sand_duping", sandDuping);
    }

    public static void set(String entry, String value) {
        APPLIERS.get(entry).accept(value);
    }

    public static boolean has(String entry) {
        return APPLIERS.containsKey(entry);
    }
}
