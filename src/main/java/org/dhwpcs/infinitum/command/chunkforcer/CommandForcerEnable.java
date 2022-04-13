package org.dhwpcs.infinitum.command.chunkforcer;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.BooleanArgument;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.executors.EntityCommandExecutor;
import org.bukkit.entity.Entity;
import org.dhwpcs.infinitum.Infinitum;

public class CommandForcerEnable implements EntityCommandExecutor {
    private final Infinitum infinitum;

    public CommandForcerEnable(Infinitum infinitum) {

        this.infinitum = infinitum;
    }

    public static CommandAPICommand create(Infinitum infinitum) {
        return new CommandAPICommand("enable")
                .withArguments(new BooleanArgument("global"))
                .withShortDescription("Enable the chunk forcer")
                .executesEntity(new CommandForcerEnable(infinitum));
    }

    @Override
    public void run(Entity commandSender, Object[] objects) throws WrapperCommandSyntaxException {
        boolean isGlobal = (boolean) objects[0];
        if(isGlobal) {
            infinitum.getWorld().getForcer().enableGlobal(commandSender);
        } else {
            infinitum.getWorld().getForcer().enable(commandSender, commandSender.getWorld());
        }
    }
}
