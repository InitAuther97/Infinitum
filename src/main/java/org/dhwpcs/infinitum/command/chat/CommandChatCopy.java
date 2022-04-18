package org.dhwpcs.infinitum.command.chat;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.executors.PlayerCommandExecutor;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.entity.Player;
import org.dhwpcs.infinitum.chat.data.Message;
import org.dhwpcs.infinitum.Infinitum;

public class CommandChatCopy implements PlayerCommandExecutor {

    private final Infinitum infinitum;

    public CommandChatCopy(Infinitum infinitum) {
        this.infinitum = infinitum;
    }

    public static CommandAPICommand create(Infinitum infinitum) {
        return new CommandAPICommand("copy")
                .withShortDescription("Copy a chat content")
                .executesPlayer(new CommandChatCopy(infinitum));
    }

    @Override
    public void run(Player player, Object[] objects) throws WrapperCommandSyntaxException {
        Message msg = infinitum.getChat().getGlobal().getMessageLocal().get(player);
        if(msg == null) {
            infinitum.getI18n().sendMessage("command.chat.id.not_set", player);
        } else if(msg.message() instanceof TextComponent it) {
            infinitum.getI18n().sendMessage("command.chat.copy.button", player, it.content());
        } else {
            infinitum.getI18n().sendMessage("command.chat.copy.unsupported_type", player);
        }
    }
}
