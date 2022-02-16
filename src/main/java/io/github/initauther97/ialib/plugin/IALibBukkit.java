package io.github.initauther97.ialib.plugin;

import io.github.initauther97.ialib.IALib;
import io.github.initauther97.ialib.event.LibConstructionEvent;
import org.bukkit.plugin.PluginLoadOrder;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.annotation.plugin.*;
import org.bukkit.plugin.java.annotation.plugin.author.Author;

@Plugin(name = "IALib", version = "1.0")
@Description("Utilities for my PaperShelled plugins")
@Author("InitAuther97")
@Website("github.com/InitAuther97/IALib")
@ApiVersion(ApiVersion.Target.v1_17)
@LoadOrder(PluginLoadOrder.STARTUP)
public class IALibBukkit extends JavaPlugin {

    @Override
    public void onLoad() {
        IALib.checkPreload();
    }

    @Override
    public void onEnable() {
        getServer().getPluginManager().callEvent(new LibConstructionEvent(new IALib()));
    }

    @Override
    public void onDisable() {
    }

}
