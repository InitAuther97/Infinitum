package org.dhwpcs.infinitum.world;

import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.SetMultimap;
import dev.jorel.commandapi.CommandAPI;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.dhwpcs.infinitum.Infinitum;
import io.github.initauther97.nugget.util.Pos2D;

import java.util.*;

public class ChunkForcer {

    final SetMultimap<NamespacedKey, Pos2D> forced = MultimapBuilder.hashKeys().hashSetValues().build();
    private final SetMultimap<NamespacedKey, Pos2D> skip = MultimapBuilder.hashKeys().hashSetValues().build();
    private final Infinitum instance;
    private final Set<NamespacedKey> enabled = new HashSet<>();

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
        if(enabled.contains(lvl.getKey())) {
            instance.getI18n().sendMessage("command.cf.enabled", sender, lvl.getKey());
            return;
        }
        instance.getI18n().broadcast("command.cf.enable", asKey(lvl));
        Set<Pos2D> cached = forced.get(asKey(lvl));
        for(Pos2D p: cached) {
            if(!lvl.addPluginChunkTicket(p.x(), p.z(), instance)) {
                instance.getI18n().sendMessage("command.cf.duplicate", sender, p);
                skip.put(asKey(lvl), p);
            }
        }
        enabled.add(lvl.getKey());
        instance.getI18n().sendMessage("command.cf.enable_success", sender, lvl.getKey());
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
        if(!enabled.contains(lvl.getKey())) {
            instance.getI18n().sendMessage("command.cf.not_enabled", sender, lvl.getKey());
            return;
        }
        instance.getI18n().broadcast("command.cf.disable", asKey(lvl));
        Set<Pos2D> skipped = skip.get(asKey(lvl));
        Set<Pos2D> cached = forced.get(asKey(lvl));
        for(Pos2D p: cached) {
            if(skipped.contains(p)) {
                instance.getI18n().sendMessage("command.cf.skip", sender, p);
            } else if(!lvl.removePluginChunkTicket(p.x(), p.z(), instance)) {
                instance.getI18n().sendMessage("command.cf.unforce_failed", sender, p);
            }
        }
        instance.getI18n().sendMessage("command.cf.disable_success", sender, lvl.getKey());
    }

    public void disableGlobal(CommandSender sender) {
        for (World w : sender.getServer().getWorlds()) {
            disable(sender, w);
        }
    }

    public boolean isEnabled(NamespacedKey lvl) {
        return enabled.contains(lvl);
    }

    public void fromConfig(ConfigurationSection section) {
        for (String key : section.getKeys(false)) {
            List<String> stringList = section.getStringList(key);
            for(String chunk : stringList) {
                forced.put(NamespacedKey.fromString(key), Pos2D.fromString(chunk));
            }
        }
    }

    public void saveConfig(ConfigurationSection section) {
        for(NamespacedKey k1 : forced.keys()) {
            Set<Pos2D> set = forced.get(k1);
            List<String> lst = new ArrayList<>(set.size());
            for(Pos2D pos : forced.get(k1)) {
                lst.add(pos.toString());
            }
            section.set(k1.asString(), lst);
        }
    }

}
