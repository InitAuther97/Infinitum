package org.dhwpcs.infinitum.papershelled;

import cn.apisium.papershelled.annotation.Mixin;
import cn.apisium.papershelled.plugin.PaperShelledPlugin;
import cn.apisium.papershelled.plugin.PaperShelledPluginDescription;
import cn.apisium.papershelled.plugin.PaperShelledPluginLoader;
import io.github.initauther97.swalf.TabExecutorDispatcher;
import org.bukkit.event.EventHandler;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.annotation.command.Command;
import org.bukkit.plugin.java.annotation.command.Commands;
import org.bukkit.plugin.java.annotation.dependency.Dependency;
import org.bukkit.plugin.java.annotation.dependency.DependsOn;
import org.bukkit.plugin.java.annotation.plugin.ApiVersion;
import org.bukkit.plugin.java.annotation.plugin.Description;
import org.bukkit.plugin.java.annotation.plugin.Plugin;
import org.bukkit.plugin.java.annotation.plugin.Website;
import org.bukkit.plugin.java.annotation.plugin.author.Author;
import org.dhwpcs.infinitum.Constants;
import org.dhwpcs.infinitum.GlobalConfig;
import org.dhwpcs.infinitum.chat.ChatEventListener;
import org.dhwpcs.infinitum.command.CommandAcknowledge;
import org.dhwpcs.infinitum.command.CommandLanguage;
import org.dhwpcs.infinitum.command.vote.CommandVoteCreate;
import org.dhwpcs.infinitum.command.vote.CommandVoteParticipate;
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
@Commands({
        @Command(
                name = "infpaper",
                desc = "Command root for InfPaper",
                aliases = {"aikarisgod", "aig", "inf"},
                usage = "/infpaper <vote/acknowledge/language> ..."
        )
})
@ApiVersion(ApiVersion.Target.v1_17)
@DependsOn(@Dependency("CommandAPI"))
@Mixin({
        MixinExplosion.class,
        MixinEntityFallingBlock.class,
        MixinMinecraftServer.class
})
public class Infinitum extends PaperShelledPlugin {

    public Infinitum(PaperShelledPluginLoader loader, PaperShelledPluginDescription paperShelledDescription, PluginDescriptionFile description, File file) {
        super(loader, paperShelledDescription, description, file);
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        extractAssets();
        getLog4JLogger().info("Begin config loading");
        GlobalConfig.read(getConfig());

        getLog4JLogger().info("Begin text loading");
        Constants.initAdventure(
                getDataFolder().toPath().resolve("assets/infpaper/text"),
                GlobalConfig::getLanguage, GlobalConfig::getConsoleLang);

        getLog4JLogger().info("Begin command registration");
        TabExecutorDispatcher dispatcher = new TabExecutorDispatcher("infpaper");
        dispatcher.registerCommand(new CommandAcknowledge(), "acknowledge");
        dispatcher.registerCommand(new CommandVoteCreate(), "create", "vote");
        dispatcher.registerCommand(new CommandVoteParticipate(), "participate", "vote");
        dispatcher.registerCommand(new CommandLanguage(), "language");
        getServer().getPluginCommand("infpaper").setExecutor(dispatcher);
        getServer().getPluginCommand("infpaper").setTabCompleter(dispatcher);

        getLog4JLogger().info("InfPaper initialized.");
        getServer().getPluginManager().registerEvents(new Infinitum.Listener(), this);
    }

    private void extractAssets() {
        saveResource("assets/infpaper/text/root.yml", false);
        saveResource("assets/infpaper/text/lang/en_us.yml", false);
        saveResource("assets/infpaper/text/lang/zh_cn.yml", false);
    }

    @Override
    public void onDisable() {
        getLog4JLogger().info("Begin config saving");
        GlobalConfig.write(getConfig());
        saveConfig();
    }

    private class Listener implements org.bukkit.event.Listener {
        @EventHandler
        public void onServerStartup(ServerLoadEvent event) {
            getServer().getPluginManager().registerEvents(new ChatEventListener(Infinitum.this), Infinitum.this);
            getServer().getScheduler().scheduleSyncRepeatingTask(Infinitum.this, VotingContext.TICKING_INTERFACE, 0, 20);
        }
    }
}