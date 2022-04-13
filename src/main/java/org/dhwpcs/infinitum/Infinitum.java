package org.dhwpcs.infinitum;

import cn.apisium.papershelled.annotation.Mixin;
import cn.apisium.papershelled.plugin.PaperShelledPlugin;
import cn.apisium.papershelled.plugin.PaperShelledPluginDescription;
import cn.apisium.papershelled.plugin.PaperShelledPluginLoader;
import io.github.initauther97.nugget.NuggetLib;
import io.github.initauther97.nugget.file.FileManager;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.annotation.dependency.Dependency;
import org.bukkit.plugin.java.annotation.dependency.DependsOn;
import org.bukkit.plugin.java.annotation.plugin.ApiVersion;
import org.bukkit.plugin.java.annotation.plugin.Description;
import org.bukkit.plugin.java.annotation.plugin.Plugin;
import org.bukkit.plugin.java.annotation.plugin.Website;
import org.bukkit.plugin.java.annotation.plugin.author.Author;
import org.dhwpcs.infinitum.chat.InfinitumChat;
import org.dhwpcs.infinitum.command.CommandInf;
import org.dhwpcs.infinitum.config.MixinConfig;
import org.dhwpcs.infinitum.i18n.I18n;
import org.dhwpcs.infinitum.mixin.MixinEntityFallingBlock;
import org.dhwpcs.infinitum.mixin.MixinExplosion;
import org.dhwpcs.infinitum.mixin.MixinMinecraftServer;
import org.dhwpcs.infinitum.network.InfinitumNetwork;
import org.dhwpcs.infinitum.statistics.InfinitumStatistics;
import org.dhwpcs.infinitum.voting.InfinitumVoting;
import org.dhwpcs.infinitum.world.InfinitumWorld;

import java.io.File;
import java.io.IOException;

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
        MixinMinecraftServer.class,
})
public class Infinitum extends PaperShelledPlugin {

    private NuggetLib nugget;
    private final FileManager file = new FileManager(getDataFolder().toPath());
    private final I18n i18n = new I18n();
    private final InfinitumNetwork network = new InfinitumNetwork(this);
    private final InfinitumChat chat = new InfinitumChat(this);
    private final InfinitumStatistics statistics = new InfinitumStatistics(this);
    private final InfinitumWorld world = new InfinitumWorld(this);
    private final InfinitumVoting voting = new InfinitumVoting(this);

    public Infinitum(PaperShelledPluginLoader loader, PaperShelledPluginDescription paperShelledDescription, PluginDescriptionFile description, File file) {
        super(loader, paperShelledDescription, description, file);
    }

    public static String version() {
        return "1.0";
    }

    @Override
    public void onLoad() {
        NuggetLib.checkPreload();
        nugget = new NuggetLib();
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        extractAssets();
        getLogger().info("Begin config loading");
        try {
            i18n.initialize(
                    file.resolve("assets","infpaper","text"),
                    getConfig().getConfigurationSection("i18n"),
                    nugget);
            network.initialize(getConfig().getConfigurationSection("network"));
            statistics.initialize(getConfig().getConfigurationSection("statistics"));
            world.initialize(getConfig().getConfigurationSection("world"));
            MixinConfig.read(getConfig().getConfigurationSection("mixin"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        getLogger().info("Begin command registration");
        CommandInf.create(this).register();
        getLogger().info("InfPaper initialized.");
        network.bootstrap();
    }

    private void extractAssets() {
        saveResource("assets/infpaper/text/root.yml", false);
        saveResource("assets/infpaper/text/lang/en_us.yml", false);
        saveResource("assets/infpaper/text/lang/zh_cn.yml", false);
    }

    @Override
    public void onDisable() {
        getLogger().info("Begin config saving");
        try {
            statistics.save(getConfig().getConfigurationSection("statistics"));
            world.save(getConfig().getConfigurationSection("world"));
            MixinConfig.save(getConfig().getConfigurationSection("mixin"));
            network.stop();
            saveConfig();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public InfinitumNetwork getNetwork() {
        return network;
    }

    public InfinitumWorld getWorld() {
        return world;
    }

    public InfinitumStatistics getStatistics() {
        return statistics;
    }

    public InfinitumVoting getVoting() {
        return voting;
    }

    public InfinitumChat getChat() {
        return chat;
    }

    public I18n getI18n() {
        return i18n;
    }

    public FileManager getFileManager() {
        return file;
    }

    public NuggetLib getLib() {
        return nugget;
    }
}