package org.dhwpcs.infinitum.command.vote;

import dev.jorel.commandapi.CommandAPICommand;
import org.dhwpcs.infinitum.Infinitum;

public class CommandVote {
    public static CommandAPICommand create(Infinitum instance) {
        return new CommandAPICommand("vote")
                .withShortDescription("Vote stuffs")
                .withSubcommand(CommandVoteCreate.create(instance))
                .withSubcommand(CommandVoteParticipate.create(instance));
    }
}
