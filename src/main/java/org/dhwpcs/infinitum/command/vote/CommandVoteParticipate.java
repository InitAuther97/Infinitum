package org.dhwpcs.infinitum.command.vote;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.StringArgument;
import dev.jorel.commandapi.arguments.UUIDArgument;
import dev.jorel.commandapi.executors.PlayerCommandExecutor;
import org.bukkit.entity.Player;
import org.dhwpcs.infinitum.Infinitum;
import org.dhwpcs.infinitum.voting.VoteParticipatingResult;
import org.dhwpcs.infinitum.voting.VoteType;

import java.util.UUID;

public class CommandVoteParticipate implements PlayerCommandExecutor {

    private final Infinitum infinitum;

    public CommandVoteParticipate(Infinitum infinitum) {
        this.infinitum = infinitum;
    }

    public static CommandAPICommand create(Infinitum infinitum) {
        CommandVoteParticipate instance = new CommandVoteParticipate(infinitum);
        return new CommandAPICommand("participate")
                .withArguments(
                        new UUIDArgument("voteid").replaceSuggestions(ArgumentSuggestions.strings(
                                i -> infinitum.getVoting().getAllOngoings().stream().map(UUID::toString).toArray(String[]::new))),
                        new StringArgument("attitude").replaceSuggestions(ArgumentSuggestions.strings(
                                i -> VoteType.SUGGESTED_INPUT.toArray(String[]::new)
                        )))
                .withShortDescription("Participate in a vote.")
                .executesPlayer(new CommandVoteParticipate(infinitum));
    }

    @Override
    public void run(Player sender, Object[] args) {
        if (args == null) {
            return;
        }
        UUID uid = (UUID) args[0];
        VoteParticipatingResult result = infinitum.getVoting().vote(sender.toString(), (UUID) args[0], (VoteType) args[1], sender.isOp());
        infinitum.getI18n().sendMessage(switch (result.type()) {
            case SUCCESS -> "command.vote.participate.success";
            case ALREADY_VOTED -> "command.vote.participate.already_voted";
            case NO_ONGOING -> "command.vote.participate.no_ongoing";
            case NOT_MATCH -> "command.vote.participate.not_match";
        }, sender);
    }
}
