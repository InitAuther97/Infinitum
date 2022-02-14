package org.dhwpcs.infinitum.command.chat;

import dev.jorel.commandapi.CommandAPICommand;

public class CommandChat {
    public static CommandAPICommand create() {
        return new CommandAPICommand("chat")
                .withShortDescription("Infinitum chat additions")
                .withSubcommand()
    }
}
