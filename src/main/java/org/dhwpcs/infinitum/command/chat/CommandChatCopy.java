package org.dhwpcs.infinitum.command.chat;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.executors.PlayerCommandExecutor;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.entity.Player;
import org.dhwpcs.infinitum.Global;
import org.dhwpcs.infinitum.I18n;
import org.dhwpcs.infinitum.chat.data.Message;

public class CommandChatCopy implements PlayerCommandExecutor {

    public static CommandAPICommand create() {
        return new CommandAPICommand("copy")
                .withShortDescription("Copy a chat content")
                .executesPlayer(new CommandChatCopy());
    }

    @Override
    public void run(Player player, Object[] objects) throws WrapperCommandSyntaxException {
        Message msg = Global.getMessageLocal().get(player);
        if(msg == null) {
            I18n.sendMessage("command.chat.copy.not_set", player);
        } else if(msg.message() instanceof TextComponent it) {
            I18n.sendMessage("command.chat.copy.button", player, it.content());
        } else {
            I18n.sendMessage("command.chat.copy.unsupported_type", player);
        }
    }
}
