package org.dhwpcs.infinitum.command.chunkforcer;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.Location2DArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.executors.CommandExecutor;
import io.github.initauther97.ialib.command.suggestion.Suggestor;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.dhwpcs.infinitum.Global;
import org.dhwpcs.infinitum.I18n;
import org.dhwpcs.infinitum.chunkforcer.ChunkForcer;
import org.dhwpcs.infinitum.util.Pos2D;

public class CommandForcerRemove implements CommandExecutor {
    public static CommandAPICommand create() {
        return new CommandAPICommand("remove")
                .withArguments(new Location2DArgument("chunkPos"))
                .withArguments(new StringArgument("worldIn")
                        .replaceSuggestionsT(Suggestor.suggestWorldKeys())
                )
                .withShortDescription("Remove a chunk to load")
                .executes(new CommandForcerRemove());
    }

    @Override
    public void run(CommandSender commandSender, Object[] objects) throws WrapperCommandSyntaxException {
        Pos2D pos = Pos2D.flattenLocation((Location) objects[0]);
        NamespacedKey key = NamespacedKey.fromString((String) objects[1]);
        if(key == null) {
            I18n.sendMessage("command.cf.illegal_dim", commandSender, objects[1]);
            return;
        }
        World world = commandSender.getServer().getWorld(key);
        if(world == null) {
            I18n.sendMessage("command.cf.illegal_dim", commandSender, key);
            return;
        }
        ChunkForcer cf = Global.getChunkForcer();
        if(cf.isEnabled()) {
            I18n.sendMessage("command.cf.already_enabled", commandSender, key);
            return;
        }
        cf.addChunk(world, pos);
    }
}
