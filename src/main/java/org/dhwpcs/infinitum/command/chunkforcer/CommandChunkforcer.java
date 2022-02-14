package org.dhwpcs.infinitum.command.chunkforcer;

import dev.jorel.commandapi.CommandAPICommand;

public class CommandChunkforcer {
    public static CommandAPICommand create() {
        return new CommandAPICommand("chunkforcer")
                .withAliases("cf","forcer")
                .withShortDescription("Chunk force-loader")
                .withSubcommand(CommandForcerAdd.create())
                .withSubcommand(CommandForcerRemove.create())
                .withSubcommand(CommandForcerDisable.create())
                .withSubcommand(CommandForcerEnable.create());
    }
}
