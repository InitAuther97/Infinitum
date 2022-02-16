package org.dhwpcs.infinitum.command.chat;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.executors.PlayerCommandExecutor;

import org.bukkit.entity.Player;
import org.dhwpcs.infinitum.Global;
import org.dhwpcs.infinitum.I18n;
import org.dhwpcs.infinitum.chat.data.Message;

public class CommandChatId implements PlayerCommandExecutor {
    public static CommandAPICommand create() {
        return new CommandAPICommand("id")
                .withShortDescription("Specific the target for the next operation")
                .withArguments(new IntegerArgument("id"))
                .executesPlayer(new CommandChatId());
    }

    @Override
    public void run(Player player, Object[] objects) throws WrapperCommandSyntaxException {
        int id = (int) objects[0];
        Message msg = Global.getHistory().getMessage(id);
        if(msg == null) {
            I18n.sendMessage("command.chat.id.illegal", player, id);
        } else {
            Global.getMessageLocal().set(player, msg);
            I18n.sendMessage("command.chat.id.set", player);
        }
    }
}
