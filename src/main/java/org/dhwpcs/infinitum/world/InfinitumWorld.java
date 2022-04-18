package org.dhwpcs.infinitum.world;

import org.bukkit.configuration.ConfigurationSection;
import org.dhwpcs.infinitum.Infinitum;

public class InfinitumWorld {
    private final ChunkForcer forcer;
    public InfinitumWorld(Infinitum infinitum) {
        forcer = new ChunkForcer(infinitum);
    }

    public ChunkForcer getForcer() {
        return forcer;
    }

    public void initialize(ConfigurationSection world) {
        ConfigurationSection forcer = world.getConfigurationSection("chunkforcer");
        this.forcer.fromConfig(forcer);
    }

    public void save(ConfigurationSection section) {
        ConfigurationSection forcer = section.getConfigurationSection("chunkforcer");
        this.forcer.saveConfig(forcer);
    }
}
