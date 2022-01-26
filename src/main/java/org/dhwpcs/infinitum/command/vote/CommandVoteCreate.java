package org.dhwpcs.infinitum.command.vote;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import dev.jorel.commandapi.executors.PlayerCommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.dhwpcs.infinitum.voting.Vote;
import org.dhwpcs.infinitum.voting.VoteRequestResult;
import org.dhwpcs.infinitum.voting.VotingContext;
import org.dhwpcs.infinitum.voting.callback.IVoteCallback;

import static org.dhwpcs.infinitum.Constants.TEXTS;

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

    static void sendMessage(String entry, CommandSender sender, Object... args) {
        sender.sendMessage(TEXTS.format(entry, sender, args));
    }

    private static IVoteCallback createVoteEndedTask(CommandSender sender) {
        return (entry, result) -> {
            String ent = switch (result) {
                case ACCEPT -> "vote.result.accept";
                case DENY -> "vote.result.reject";
            };
            for (Player player : sender.getServer().getOnlinePlayers()) {
                sendMessage(ent, player,
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
                sendMessage("vote.create.success", sender);
                for (Player player : sender.getServer().getOnlinePlayers()) {
                    sendMessage("vote.info", player,
                            sender.getName(),
                            vote.getEntry().getVoteId(),
                            vote.getEntry().getDescription(),
                            vote.getUid().toString()
                    );
                }
            }
            case INCORRECT_ARGUMENT -> sendMessage("vote.create.incorrect_argument", sender);
            case ALREADY_ONGOING -> {
                Vote vte = (Vote) result.result()[0];
                sendMessage("vote.create.already_ongoing", sender, vte.getUid().toString());
            }
            case UNKNOWN_VOTE -> {
                String id = (String) result.result()[0];
                sendMessage("vote.create.unknown_vote", sender, id);
            }
        }
    }
}
