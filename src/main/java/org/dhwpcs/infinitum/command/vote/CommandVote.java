package org.dhwpcs.infinitum.command.vote;

import dev.jorel.commandapi.CommandAPICommand;

public class CommandVote {
    public static CommandAPICommand create() {
        return new CommandAPICommand("vote")
                .withShortDescription("Vote stuffs")
                .withSubcommand(CommandVoteCreate.create())
                .withSubcommand(CommandVoteParticipate.create());
    }
}
