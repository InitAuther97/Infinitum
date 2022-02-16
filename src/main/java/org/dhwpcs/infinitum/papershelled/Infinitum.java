package org.dhwpcs.infinitum.papershelled;

import cn.apisium.papershelled.annotation.Mixin;
import cn.apisium.papershelled.plugin.PaperShelledPlugin;
import cn.apisium.papershelled.plugin.PaperShelledPluginDescription;
import cn.apisium.papershelled.plugin.PaperShelledPluginLoader;
import io.github.initauther97.ialib.IALib;
import io.github.initauther97.ialib.event.LibConstructionEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.annotation.dependency.Dependency;
import org.bukkit.plugin.java.annotation.dependency.DependsOn;
import org.bukkit.plugin.java.annotation.plugin.ApiVersion;
import org.bukkit.plugin.java.annotation.plugin.Description;
import org.bukkit.plugin.java.annotation.plugin.Plugin;
import org.bukkit.plugin.java.annotation.plugin.Website;
import org.bukkit.plugin.java.annotation.plugin.author.Author;
import org.dhwpcs.infinitum.Global;
import org.dhwpcs.infinitum.chat.ChatEventListener;
import org.dhwpcs.infinitum.command.CommandInf;
import org.dhwpcs.infinitum.mixin.MixinEntityFallingBlock;
import org.dhwpcs.infinitum.mixin.MixinExplosion;
import org.dhwpcs.infinitum.mixin.MixinMinecraftServer;
import org.dhwpcs.infinitum.voting.VotingContext;

import java.io.File;

@SuppressWarnings("ReferenceToMixin")
@Plugin(name = "Infinitum", version = "1.0")
@Description("PaperShelled plugin for DHW Inf")
@Author("InitAuther97")
@Website("github.com/InitAuther97/InfPaper")
@ApiVersion(ApiVersion.Target.v1_17)
@DependsOn({@Dependency("CommandAPI"), @Dependency("IALib")})
@Mixin({
        MixinExplosion.class,
        MixinEntityFallingBlock.class,
        MixinMinecraftServer.class
})
public class Infinitum extends PaperShelledPlugin {

    private IALib ialib;

    public Infinitum(PaperShelledPluginLoader loader, PaperShelledPluginDescription paperShelledDescription, PluginDescriptionFile description, File file) {
        super(loader, paperShelledDescription, description, file);
    }

    public static String version() {
        return "1.2";
    }

    @Override
    public void onLoad() {
        getServer().getPluginManager().registerEvents(new Infinitum.Listener(), this);
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        extractAssets();
        getLog4JLogger().info("Begin config loading");
        Global.load(getConfig(), this, ialib);
        getLog4JLogger().info("Begin command registration");
        CommandInf.create().register();
        getLog4JLogger().info("InfPaper initialized.");
    }

    private void extractAssets() {
        saveResource("assets/infpaper/text/root.yml", false);
        saveResource("assets/infpaper/text/lang/en_us.yml", false);
        saveResource("assets/infpaper/text/lang/zh_cn.yml", false);
    }

    @Override
    public void onDisable() {
        getLog4JLogger().info("Begin config saving");
        Global.store(getConfig(), this);
        saveConfig();
    }

    private class Listener implements org.bukkit.event.Listener {

        @EventHandler
        public void onLibSetup(LibConstructionEvent event) {
            ialib = event.getLibrary();
        }

        @EventHandler
        public void onServerStartup(ServerLoadEvent event) {
            getServer().getPluginManager().registerEvents(new ChatEventListener(Infinitum.this), Infinitum.this);
            getServer().getScheduler().scheduleSyncRepeatingTask(Infinitum.this, VotingContext.TICKING_INTERFACE, 0, 20);
            Global.server.start();
        }
    }
}