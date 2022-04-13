package org.dhwpcs.infinitum.command.chunkforcer;

import dev.jorel.commandapi.CommandAPICommand;
import org.dhwpcs.infinitum.Infinitum;

public class CommandChunkforcer {
    public static CommandAPICommand create(Infinitum infinitum) {
        return new CommandAPICommand("chunkforcer")
                .withAliases("cf","forcer")
                .withShortDescription("Chunk force-loader")
                .withSubcommand(CommandForcerAdd.create(infinitum))
                .withSubcommand(CommandForcerRemove.create(infinitum))
                .withSubcommand(CommandForcerDisable.create(infinitum))
                .withSubcommand(CommandForcerEnable.create(infinitum));
    }
}
