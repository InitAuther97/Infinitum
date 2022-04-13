package org.dhwpcs.infinitum.command;

import dev.jorel.commandapi.CommandAPICommand;
import org.dhwpcs.infinitum.command.chat.CommandChat;
import org.dhwpcs.infinitum.command.chunkforcer.CommandChunkforcer;
import org.dhwpcs.infinitum.command.vote.CommandVote;
import org.dhwpcs.infinitum.Infinitum;

public class CommandInf {
    public static CommandAPICommand create(Infinitum infinitum) {
        return new CommandAPICommand("infinitum")
                .withAliases("inf","aikarisgod","aig")
                .withShortDescription("Command root for Infinitum")
                .withSubcommand(CommandAcknowledge.create(infinitum))
                .withSubcommand(CommandLanguage.create(infinitum))
                .withSubcommand(CommandVote.create(infinitum))
                .withSubcommand(CommandChunkforcer.create(infinitum))
                .withSubcommand(CommandChat.create(infinitum));
    }
}
