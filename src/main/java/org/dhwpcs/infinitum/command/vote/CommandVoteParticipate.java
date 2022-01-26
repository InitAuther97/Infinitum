package org.dhwpcs.infinitum.command.vote;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.StringArgument;
import dev.jorel.commandapi.arguments.UUIDArgument;
import dev.jorel.commandapi.executors.PlayerCommandExecutor;
import org.bukkit.entity.Player;
import org.dhwpcs.infinitum.Constants;
import org.dhwpcs.infinitum.voting.VoteParticipatingResult;
import org.dhwpcs.infinitum.voting.VoteType;
import org.dhwpcs.infinitum.voting.VotingContext;

import java.util.UUID;

public class CommandVoteParticipate implements PlayerCommandExecutor {

    public static CommandAPICommand create() {
        return new CommandAPICommand("participate")
                .withArguments(
                        new UUIDArgument("voteid").replaceSuggestions(
                                i -> VotingContext.getAllOngoings().stream().map(UUID::toString).toArray(String[]::new)),
                        new StringArgument("attitude").replaceSuggestions(
                                i -> VoteType.SUGGESTED_INPUT.toArray(String[]::new)
                        ))
                .withShortDescription("Participate in a vote.")
                .executesPlayer(new CommandVoteParticipate());
    }

    @Override
    public void run(Player sender, Object[] args) {
        if (args == null) {
            return;
        }
        UUID uid = (UUID) args[0];
        VoteParticipatingResult result = VotingContext.vote(sender.toString(), (UUID) args[0], (VoteType) args[1], sender.isOp());
        sender.sendMessage(Constants.TEXTS.format(switch (result.type()) {
            case SUCCESS -> "vote.participate.success";
            case ALREADY_VOTED -> "vote.participate.already_voted";
            case NO_ONGOING -> "vote.participate.no_ongoing";
            case NOT_MATCH -> "vote.participate.not_match";
        }, sender));
    }
}
