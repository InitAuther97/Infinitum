package org.dhwpcs.infinitum.command.chunkforcer;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.BooleanArgument;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.executors.EntityCommandExecutor;
import org.bukkit.entity.Entity;
import org.dhwpcs.infinitum.Global;

public class CommandForcerDisable implements EntityCommandExecutor {
    public static CommandAPICommand create() {
        return new CommandAPICommand("disable")
                .withArguments(new BooleanArgument("global"))
                .withShortDescription("Disable the chunk forcer")
                .executesEntity(new CommandForcerDisable());
    }

    @Override
    public void run(Entity entity, Object[] objects) throws WrapperCommandSyntaxException {
        boolean isGlobal = (boolean) objects[0];
        if(isGlobal) {
            Global.getChunkForcer().disableGlobal(entity);
        } else {
            Global.getChunkForcer().disable(entity, entity.getWorld());
        }
    }
}
