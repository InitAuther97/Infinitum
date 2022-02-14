package org.dhwpcs.infinitum.command.chunkforcer;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.Location2DArgument;
import dev.jorel.commandapi.arguments.LocationType;
import dev.jorel.commandapi.arguments.StringArgument;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.executors.CommandExecutor;
import dev.jorel.commandapi.wrappers.Location2D;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.dhwpcs.infinitum.Global;
import org.dhwpcs.infinitum.I18n;
import org.dhwpcs.infinitum.chunkforcer.ChunkForcer;
import org.dhwpcs.infinitum.util.Pos2D;

public class CommandForcerAdd implements CommandExecutor {
    public static CommandAPICommand create() {
        return new CommandAPICommand("add")
                .withShortDescription("Add a chunk in to be forced list")
                .withArguments(new Location2DArgument("chunkPos", LocationType.BLOCK_POSITION))
                .withArguments(new StringArgument("worldIn").replaceSuggestions(info ->
                    info.sender().getServer().getWorlds().stream()
                            .map(w -> w.getKey().toString()).toArray(String[]::new)
                ))
                .executes(new CommandForcerAdd());
    }

    @Override
    public void run(CommandSender commandSender, Object[] objects) throws WrapperCommandSyntaxException {
        Location2D l2d = (Location2D) objects[0];
        String dimension = (String) objects[1];
        NamespacedKey key = NamespacedKey.fromString(dimension);
        if(key == null) {
            I18n.sendMessage("command.cf.illegal_dim", commandSender, dimension);
            return;
        }
        World world = commandSender.getServer().getWorld(key);
        if(world == null) {
            I18n.sendMessage("command.cf.illegal_dim", commandSender, dimension);
            return;
        }
        ChunkForcer cf = Global.getChunkForcer();
        if(cf.isEnabled()) {
            I18n.sendMessage("command.cf.already_enabled", commandSender, dimension);
            return;
        }
        cf.addChunk(world, Pos2D.flattenLocation(l2d));
    }
}
