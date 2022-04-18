package org.dhwpcs.infinitum.command.vote;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import dev.jorel.commandapi.executors.PlayerCommandExecutor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.dhwpcs.infinitum.i18n.I18n;
import org.dhwpcs.infinitum.Infinitum;
import org.dhwpcs.infinitum.voting.Vote;
import org.dhwpcs.infinitum.voting.VoteRequestResult;
import org.dhwpcs.infinitum.voting.callback.IVoteCallback;

public class CommandVoteCreate implements PlayerCommandExecutor {

    private final Infinitum infinitum;

    public CommandVoteCreate(Infinitum instance) {
        this.infinitum = instance;
    }

    public static CommandAPICommand create(Infinitum instance) {
        return new CommandAPICommand("create")
                .withArguments(
                        new StringArgument("type")
                                .replaceSuggestions(ArgumentSuggestions.strings(instance.getVoting().getVoteIds().toArray(String[]::new))),
                        new IntegerArgument("period")
                                .replaceSuggestions(ArgumentSuggestions.strings("10", "20")))
                .withShortDescription("Create a vote.")
                .executesPlayer(new CommandVoteCreate(instance));
    }

    private IVoteCallback createVoteEndedTask(CommandSender sender) {
        return (entry, result) -> {
            String ent = switch (result) {
                case ACCEPT -> "command.vote.result.accept";
                case DENY -> "command.vote.result.reject";
            };
            for (Player player : sender.getServer().getOnlinePlayers()) {
                infinitum.getI18n().sendMessage(ent, player,
                        entry.getEntry().getVoteId(),
                        entry.getEntry().getDescription()
                );
            }
        };
    }

    @Override
    public void run(Player sender, Object[] args) {
        VoteRequestResult result = infinitum.getVoting().createVote((String) args[0], (Integer) args[1], createVoteEndedTask(sender));
        I18n i18n = infinitum.getI18n();
        switch (result.type()) {
            case SUCCESS -> {
                Vote vote = (Vote) result.result()[0];
                i18n.sendMessage("command.vote.create.success", sender);
                i18n.broadcast("command.vote.info",
                        sender.getName(),
                        vote.getEntry().getVoteId(),
                        vote.getEntry().getDescription(),
                        vote.getUid().toString()
                );
            }
            case INCORRECT_ARGUMENT -> i18n.sendMessage("command.vote.create.incorrect_argument", sender);
            case ALREADY_ONGOING -> {
                Vote vte = (Vote) result.result()[0];
                i18n.sendMessage("command.vote.create.already_ongoing", sender, vte.getUid().toString());
            }
            case UNKNOWN_VOTE -> {
                String id = (String) result.result()[0];
                i18n.sendMessage("command.vote.create.unknown_vote", sender, id);
            }
        }
    }
}
