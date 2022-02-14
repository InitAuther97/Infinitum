package org.dhwpcs.infinitum.command.chat;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.executors.PlayerCommandExecutor;
import org.bukkit.entity.Player;

public class CommandChatId implements PlayerCommandExecutor {
    public static CommandAPICommand create() {
        return new CommandAPICommand("id")
                .withShortDescription("Specific the target for the next operation")
                .withArguments(new IntegerArgument("id"))
                .executesPlayer(new CommandChatId());
    }

    @Override
    public void run(Player player, Object[] objects) throws WrapperCommandSyntaxException {

    }
}
