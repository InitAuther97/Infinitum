package org.dhwpcs.infinitum.command.chunkforcer;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.Location2DArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.executors.CommandExecutor;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.dhwpcs.infinitum.world.ChunkForcer;
import org.dhwpcs.infinitum.Infinitum;
import io.github.initauther97.nugget.util.Pos2D;

public class CommandForcerRemove implements CommandExecutor {
    private final Infinitum infinitum;

    public CommandForcerRemove(Infinitum infinitum) {
        this.infinitum = infinitum;
    }

    public static CommandAPICommand create(Infinitum infinitum) {
        return new CommandAPICommand("remove")
                .withArguments(new Location2DArgument("chunkPos"))
                .withArguments(new StringArgument("worldIn")
                        .replaceSuggestions(ArgumentSuggestions.strings(info ->
                            info.sender().getServer().getWorlds().stream().map(World::getKey).map(NamespacedKey::asString).toArray(String[]::new)
                        ))
                )
                .withShortDescription("Remove a chunk to load")
                .executes(new CommandForcerRemove(infinitum));
    }

    @Override
    public void run(CommandSender commandSender, Object[] objects) throws WrapperCommandSyntaxException {
        Pos2D pos = Pos2D.flattenLocation((Location) objects[0]);
        NamespacedKey key = NamespacedKey.fromString((String) objects[1]);
        if(key == null) {
            infinitum.getI18n().sendMessage("command.cf.illegal_dim", commandSender, objects[1]);
            return;
        }
        World world = commandSender.getServer().getWorld(key);
        if(world == null) {
            infinitum.getI18n().sendMessage("command.cf.illegal_dim", commandSender, key);
            return;
        }
        ChunkForcer cf = infinitum.getWorld().getForcer();
        if(cf.isEnabled(key)) {
            infinitum.getI18n().sendMessage("command.cf.already_enabled", commandSender, key);
            return;
        }
        cf.addChunk(world, pos);
        infinitum.getI18n().sendMessage("command.cf.removed", commandSender, key, pos);
    }
}
