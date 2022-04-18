package org.dhwpcs.infinitum.command.chunkforcer;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.executors.EntityCommandExecutor;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.dhwpcs.infinitum.Infinitum;

public class CommandForcerDisable implements EntityCommandExecutor {
    private final Infinitum infinitum;

    public CommandForcerDisable(Infinitum infinitum) {
        this.infinitum = infinitum;
    }

    public static CommandAPICommand create(Infinitum infinitum) {
        return new CommandAPICommand("disable")
                .withArguments(new GreedyStringArgument("worldIn").replaceSuggestions(ArgumentSuggestions.strings(info ->
                        info.sender().getServer().getWorlds().stream()
                                .map(w -> w.getKey().toString()).toArray(String[]::new)
                )))
                .withShortDescription("Disable the chunk forcer")
                .executesEntity(new CommandForcerDisable(infinitum));
    }

    @Override
    public void run(Entity entity, Object[] objects) throws WrapperCommandSyntaxException {
        String dimension = (String) objects[0];
        NamespacedKey key = NamespacedKey.fromString(dimension);
        if(key == null) {
            infinitum.getI18n().sendMessage("command.cf.illegal_dim", entity, dimension);
            return;
        }
        World world = entity.getServer().getWorld(key);
        if(world == null) {
            infinitum.getI18n().sendMessage("command.cf.illegal_dim", entity, dimension);
            return;
        }
        infinitum.getWorld().getForcer().disable(entity, entity.getWorld());
    }
}
