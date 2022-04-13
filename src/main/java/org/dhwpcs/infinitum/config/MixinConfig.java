package org.dhwpcs.infinitum.config;

import org.bukkit.configuration.ConfigurationSection;
import org.dhwpcs.infinitum.Infinitum;

import java.io.IOException;

public class MixinConfig {
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
}
