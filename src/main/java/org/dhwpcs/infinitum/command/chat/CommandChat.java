package org.dhwpcs.infinitum.command.chat;

import dev.jorel.commandapi.CommandAPICommand;
import org.dhwpcs.infinitum.Infinitum;

public class CommandChat {
    public static CommandAPICommand create(Infinitum infinitum) {
        return new CommandAPICommand("chat")
                .withShortDescription("Infinitum chat additions")
                .withSubcommand(CommandChatId.create(infinitum))
                .withSubcommand(CommandChatCopy.create(infinitum))
                .withSubcommand(CommandChatInfo.create(infinitum));
    }
}
