package org.dhwpcs.infinitum.command;

import dev.jorel.commandapi.CommandAPICommand;
import org.dhwpcs.infinitum.command.vote.CommandVote;

public class CommandInf {
    public static CommandAPICommand create() {
        return new CommandAPICommand("infinitum")
                .withAliases("inf","aikarisgod","aig")
                .withShortDescription("Command root for Infinitum")
                .withSubcommand(CommandAcknowledge.create())
                .withSubcommand(CommandLanguage.create())
                .withSubcommand(CommandVote.create());
    }
}
