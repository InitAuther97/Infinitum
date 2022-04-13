package org.dhwpcs.infinitum.world;

import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.SetMultimap;
import dev.jorel.commandapi.CommandAPI;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.dhwpcs.infinitum.Infinitum;
import io.github.initauther97.nugget.util.Pos2D;

import java.util.Set;

public class ChunkForcer {

    final SetMultimap<NamespacedKey, Pos2D> forced = MultimapBuilder.hashKeys().hashSetValues().build();
    private final SetMultimap<NamespacedKey, Pos2D> skip = MultimapBuilder.hashKeys().hashSetValues().build();
    private final Infinitum instance;
    private boolean running = false;

    static {
        CommandAPI.unregister("forceload");
    }

    public ChunkForcer(Infinitum instance) {
        this.instance = instance;
    }

    public boolean addChunkIn(World world, Pos2D blockAt) {
        Pos2D result = new Pos2D(blockAt.x() / 16,  blockAt.z() / 16);
        return forced.put(asKey(world), result);
    }

    public boolean addChunk(World lvl, Pos2D chunk) {
        return forced.put(asKey(lvl), chunk);
    }

    public void addChunks(World lvl, Set<Pos2D> chunks) {
        forced.putAll(asKey(lvl), chunks);
    }

    public Set<Pos2D> getAll(World lvl) {
        return Set.copyOf(forced.get(asKey(lvl)));
    }

    public void enable(CommandSender sender, World lvl) {
        if(running) {
            instance.getI18n().sendMessage("cf.enabled", sender);
            return;
        }
        instance.getI18n().broadcast(Bukkit.getServer(), "cf.enable", asKey(lvl));
        Set<Pos2D> cached = forced.get(asKey(lvl));
        for(Pos2D p: cached) {
            if(!lvl.addPluginChunkTicket(p.x(), p.z(), instance)) {
                instance.getI18n().sendMessage("cf.force.duplicate", sender, p);
                skip.put(asKey(lvl), p);
            }
        }
        instance.getI18n().sendMessage("cf.force.success", sender, cached.size());
    }

    public void enableGlobal(CommandSender sender) {
        for (World w : sender.getServer().getWorlds()) {
            enable(sender, w);
        }
    }

    static NamespacedKey asKey(World world) {
        return world.getKey();
    }

    public void disable(CommandSender sender, World lvl) {
        if(!running) {
            instance.getI18n().sendMessage("cf.not_enabled", sender);
            return;
        }
        instance.getI18n().broadcast(Bukkit.getServer(), "cf.disable", asKey(lvl));
        Set<Pos2D> skipped = skip.get(asKey(lvl));
        Set<Pos2D> cached = forced.get(asKey(lvl));
        for(Pos2D p: cached) {
            if(skipped.contains(p)) {
                instance.getI18n().sendMessage("cf.unforce.skip", sender, p);
            } else if(!lvl.addPluginChunkTicket(p.x(), p.z(), instance)) {
                instance.getI18n().sendMessage("cf.unforce.failed", sender, p);
            }
        }
        instance.getI18n().sendMessage("cf.unforce.success", sender, cached.size());
    }

    public void disableGlobal(CommandSender sender) {
        for (World w : sender.getServer().getWorlds()) {
            disable(sender, w);
        }
    }

    public boolean isEnabled() {
        return running;
    }

    public void fromConfig(ConfigurationSection section) {
        for(String key : section.getKeys(false)) {
            section.getStringList(key)
                    .forEach(it -> forced.put(NamespacedKey.fromString(it), Pos2D.fromString(section.getString(it))));
        }
    }

    public void saveConfig(ConfigurationSection section) {
        forced.keys().forEach(it -> section.set(it.asString(), forced.get(it)));
    }

}
