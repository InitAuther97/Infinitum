package org.dhwpcs.infinitum.command.chunkforcer;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.BooleanArgument;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.executors.EntityCommandExecutor;
import org.bukkit.entity.Entity;
import org.dhwpcs.infinitum.Infinitum;

public class CommandForcerDisable implements EntityCommandExecutor {
    private final Infinitum infinitum;

    public CommandForcerDisable(Infinitum infinitum) {
        this.infinitum = infinitum;
    }

    public static CommandAPICommand create(Infinitum infinitum) {
        return new CommandAPICommand("disable")
                .withArguments(new BooleanArgument("global"))
                .withShortDescription("Disable the chunk forcer")
                .executesEntity(new CommandForcerDisable(infinitum));
    }

    @Override
    public void run(Entity entity, Object[] objects) throws WrapperCommandSyntaxException {
        boolean isGlobal = (boolean) objects[0];
        if(isGlobal) {
            infinitum.getWorld().getForcer().disableGlobal(entity);
        } else {
            infinitum.getWorld().getForcer().disable(entity, entity.getWorld());
        }
    }
}
