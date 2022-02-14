package org.dhwpcs.infinitum.chunkforcer;

import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.SetMultimap;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

import org.bukkit.plugin.Plugin;
import org.dhwpcs.infinitum.I18n;
import org.dhwpcs.infinitum.util.Pos2D;

import java.util.Set;

public class ChunkForcer {

    private final SetMultimap<String, Pos2D> forced = MultimapBuilder.hashKeys().hashSetValues().build();
    private final SetMultimap<String, Pos2D> skip = MultimapBuilder.hashKeys().hashSetValues().build();
    private final Plugin instance;
    private boolean running = false;

    public ChunkForcer(Plugin instance) {
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
            I18n.sendMessage("cf.enabled", sender);
            return;
        }
        I18n.broadcast(Bukkit.getServer(), "cf.enable", asKey(lvl));
        Set<Pos2D> cached = forced.get(asKey(lvl));
        for(Pos2D p: cached) {
            if(!lvl.addPluginChunkTicket(p.x(), p.z(), instance)) {
                I18n.sendMessage("cf.force.duplicate", sender, p);
                skip.put(asKey(lvl), p);
            }
        }
        I18n.sendMessage("cf.force.success", sender, cached.size());
    }

    public void enableGlobal(CommandSender sender) {
        for (World w : sender.getServer().getWorlds()) {
            enable(sender, w);
        }
    }

    static String asKey(World world) {
        return world.getKey().toString();
    }

    public void disable(CommandSender sender, World lvl) {
        if(!running) {
            I18n.sendMessage("cf.not_enabled", sender);
            return;
        }
        I18n.broadcast(Bukkit.getServer(), "cf.disable", asKey(lvl));
        Set<Pos2D> skipped = skip.get(asKey(lvl));
        Set<Pos2D> cached = forced.get(asKey(lvl));
        for(Pos2D p: cached) {
            if(skipped.contains(p)) {
                I18n.sendMessage("cf.unforce.skip", sender, p);
            } else if(!lvl.addPluginChunkTicket(p.x(), p.z(), instance)) {
                I18n.sendMessage("cf.unforce.failed", sender, p);
            }
        }
        I18n.sendMessage("cf.unforce.success", sender, cached.size());
    }

    public void disableGlobal(CommandSender sender) {
        for (World w : sender.getServer().getWorlds()) {
            disable(sender, w);
        }
    }

    public boolean isEnabled() {
        return running;
    }

    public static ChunkForcer fromConfig(ConfigurationSection section, Plugin instance) {
        ChunkForcer forcer = new ChunkForcer(instance);
        if(section == null) {
            return forcer;
        }
        for(String key : section.getKeys(false)) {
            section.getStringList(key)
                    .forEach(it -> forcer.forced.put(it, Pos2D.fromString(section.getString(it))));
        }
        return forcer;
    }

    public static void saveConfig(ChunkForcer forcer, ConfigurationSection section) {
        forcer.forced.keys().forEach(it -> section.set(it, forcer.forced.get(it)));
    }

}
