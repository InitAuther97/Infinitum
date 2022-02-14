package org.dhwpcs.infinitum.command.chunkforcer;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.BooleanArgument;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.executors.EntityCommandExecutor;
import org.bukkit.entity.Entity;
import org.dhwpcs.infinitum.Global;

public class CommandForcerEnable implements EntityCommandExecutor {
    public static CommandAPICommand create() {
        return new CommandAPICommand("enable")
                .withArguments(new BooleanArgument("global"))
                .withShortDescription("Enable the chunk forcer")
                .executesEntity(new CommandForcerEnable());
    }

    @Override
    public void run(Entity commandSender, Object[] objects) throws WrapperCommandSyntaxException {
        boolean isGlobal = (boolean) objects[0];
        if(isGlobal) {
            Global.getChunkForcer().enableGlobal(commandSender);
        } else {
            Global.getChunkForcer().enable(commandSender, commandSender.getWorld());
        }
    }
}
