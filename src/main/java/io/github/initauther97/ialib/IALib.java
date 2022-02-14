package io.github.initauther97.ialib;

import cn.apisium.papershelled.plugin.PaperShelledPlugin;
import cn.apisium.papershelled.plugin.PaperShelledPluginDescription;
import cn.apisium.papershelled.plugin.PaperShelledPluginLoader;

import io.github.initauther97.ialib.adventure.AdventureWrapped;
import io.github.initauther97.ialib.damson.DamsonPlayerLocal;
import io.github.initauther97.ialib.event.LibConstructionEvent;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoadOrder;
import org.bukkit.plugin.java.annotation.plugin.*;
import org.bukkit.plugin.java.annotation.plugin.author.Author;

import java.io.File;
import java.nio.file.Path;

@Plugin(name = "IALib", version = "1.0")
@Description("Utilities for my PaperShelled plugins")
@Author("InitAuther97")
@Website("github.com/InitAuther97/IALib")
@ApiVersion(ApiVersion.Target.v1_17)
@LoadOrder(PluginLoadOrder.STARTUP)
public class IALib extends PaperShelledPlugin {

    public IALib(PaperShelledPluginLoader loader, PaperShelledPluginDescription paperShelledDescription, PluginDescriptionFile description, File file) {
        super(loader, paperShelledDescription, description, file);
    }

    @Override
    public void onLoad() {
    }

    @Override
    public void onEnable() {
        getServer().getPluginManager().callEvent(new LibConstructionEvent(this));
    }

    @Override
    public void onDisable() {
    }

    public<T> PlayerLocal<T> createPlayerLocal(Class<T> type) {
        return new DamsonPlayerLocal<>(type);
    }

    public AdventureWrapped createAdventureWrapper(Path root) {
        return new AdventureWrapped(root);
    }
}
