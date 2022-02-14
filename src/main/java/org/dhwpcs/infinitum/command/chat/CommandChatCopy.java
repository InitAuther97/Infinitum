package org.dhwpcs.infinitum.command.chat;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.executors.PlayerCommandExecutor;
import org.bukkit.entity.Player;

public class CommandChatCopy implements PlayerCommandExecutor {
    public static CommandAPICommand create() {
        return new CommandAPICommand("copy")
                .withShortDescription("Copy a chat content")
                .executesPlayer(new CommandChatCopy());
    }

    @Override
    public void run(Player player, Object[] objects) throws WrapperCommandSyntaxException {

    }
}
