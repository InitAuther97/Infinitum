package org.dhwpcs.infinitum.command.chat;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.executors.PlayerCommandExecutor;

import org.bukkit.entity.Player;
import org.dhwpcs.infinitum.chat.data.Message;
import org.dhwpcs.infinitum.Infinitum;

public class CommandChatId implements PlayerCommandExecutor {
    private final Infinitum infinitum;

    public CommandChatId(Infinitum infinitum) {
        this.infinitum = infinitum;
    }

    public static CommandAPICommand create(Infinitum infinitum) {
        return new CommandAPICommand("id")
                .withShortDescription("Specific the target for the next operation")
                .withArguments(new IntegerArgument("id"))
                .executesPlayer(new CommandChatId(infinitum));
    }

    @Override
    public void run(Player player, Object[] objects) throws WrapperCommandSyntaxException {
        int id = (int) objects[0];
        Message msg = infinitum.getChat().getGlobal().getHistory().getMessage(id);
        if(msg == null) {
            infinitum.getI18n().sendMessage("command.chat.id.illegal", player, id);
        } else {
            infinitum.getChat().getGlobal().getMessageLocal().set(player, msg);
            infinitum.getI18n().sendMessage("command.chat.id.set", player);
        }
    }
}
