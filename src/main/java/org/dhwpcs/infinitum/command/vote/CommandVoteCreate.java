package org.dhwpcs.infinitum.command.vote;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import dev.jorel.commandapi.executors.PlayerCommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.dhwpcs.infinitum.I18n;
import org.dhwpcs.infinitum.voting.Vote;
import org.dhwpcs.infinitum.voting.VoteRequestResult;
import org.dhwpcs.infinitum.voting.VotingContext;
import org.dhwpcs.infinitum.voting.callback.IVoteCallback;

public class CommandVoteCreate implements PlayerCommandExecutor {

    public static CommandAPICommand create() {
        return new CommandAPICommand("create")
                .withArguments(
                        new StringArgument("type")
                                .replaceSuggestions(i -> VotingContext.getVoteIds().toArray(String[]::new)),
                        new IntegerArgument("period")
                                .replaceSuggestions(i -> new String[]{"10","20"}))
                .withShortDescription("Create a vote.")
                .executesPlayer(new CommandVoteCreate());
    }

    private static IVoteCallback createVoteEndedTask(CommandSender sender) {
        return (entry, result) -> {
            String ent = switch (result) {
                case ACCEPT -> "vote.result.accept";
                case DENY -> "vote.result.reject";
            };
            for (Player player : sender.getServer().getOnlinePlayers()) {
                I18n.sendMessage(ent, player,
                        entry.getEntry().getVoteId(),
                        entry.getEntry().getDescription()
                );
            }
        };
    }

    @Override
    public void run(Player sender, Object[] args) {
        VoteRequestResult result = VotingContext.createVote((String) args[0], (Integer) args[1], createVoteEndedTask(sender));
        switch (result.type()) {
            case SUCCESS -> {
                Vote vote = (Vote) result.result()[0];
                I18n.sendMessage("vote.create.success", sender);
                for (Player player : sender.getServer().getOnlinePlayers()) {
                    I18n.sendMessage("vote.info", player,
                            sender.getName(),
                            vote.getEntry().getVoteId(),
                            vote.getEntry().getDescription(),
                            vote.getUid().toString()
                    );
                }
            }
            case INCORRECT_ARGUMENT -> I18n.sendMessage("vote.create.incorrect_argument", sender);
            case ALREADY_ONGOING -> {
                Vote vte = (Vote) result.result()[0];
                I18n.sendMessage("vote.create.already_ongoing", sender, vte.getUid().toString());
            }
            case UNKNOWN_VOTE -> {
                String id = (String) result.result()[0];
                I18n.sendMessage("vote.create.unknown_vote", sender, id);
            }
        }
    }
}
