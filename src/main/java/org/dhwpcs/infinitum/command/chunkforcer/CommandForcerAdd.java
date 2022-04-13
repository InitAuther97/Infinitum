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
import org.dhwpcs.infinitum.world.ChunkForcer;
import org.dhwpcs.infinitum.Infinitum;
import io.github.initauther97.nugget.util.Pos2D;

public class CommandForcerAdd implements CommandExecutor {
    private final Infinitum infinitum;

    public CommandForcerAdd(Infinitum infinitum) {
        this.infinitum = infinitum;
    }

    public static CommandAPICommand create(Infinitum infinitum) {
        return new CommandAPICommand("add")
                .withShortDescription("Add a chunk in to be forced list")
                .withArguments(new Location2DArgument("chunkPos", LocationType.BLOCK_POSITION))
                .withArguments(new StringArgument("worldIn").replaceSuggestions(info ->
                    info.sender().getServer().getWorlds().stream()
                            .map(w -> w.getKey().toString()).toArray(String[]::new)
                ))
                .executes(new CommandForcerAdd(infinitum));
    }

    @Override
    public void run(CommandSender commandSender, Object[] objects) throws WrapperCommandSyntaxException {
        Location2D l2d = (Location2D) objects[0];
        String dimension = (String) objects[1];
        NamespacedKey key = NamespacedKey.fromString(dimension);
        if(key == null) {
            infinitum.getI18n().sendMessage("command.cf.illegal_dim", commandSender, dimension);
            return;
        }
        World world = commandSender.getServer().getWorld(key);
        if(world == null) {
            infinitum.getI18n().sendMessage("command.cf.illegal_dim", commandSender, dimension);
            return;
        }
        ChunkForcer cf = infinitum.getWorld().getForcer();
        if(cf.isEnabled()) {
            infinitum.getI18n().sendMessage("command.cf.already_enabled", commandSender, dimension);
            return;
        }
        cf.addChunk(world, Pos2D.flattenLocation(l2d));
    }
}
