package org.dhwpcs.infinitum.command.chat;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.executors.PlayerCommandExecutor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.dhwpcs.infinitum.Global;
import org.dhwpcs.infinitum.chat.data.Message;
import org.dhwpcs.infinitum.Infinitum;

import java.time.temporal.Temporal;

public class CommandChatInfo implements PlayerCommandExecutor {

    private final Infinitum infinitum;

    public CommandChatInfo(Infinitum infinitum) {
        this.infinitum = infinitum;
    }

    public static CommandAPICommand create(Infinitum infinitum) {
        return new CommandAPICommand("info")
                .withShortDescription("Pull the information of the message")
                .executesPlayer(new CommandChatInfo(infinitum));
    }

    @Override
    public void run(Player player, Object[] objects) throws WrapperCommandSyntaxException {
        Message msg = infinitum.getChat().getGlobal().getMessageLocal().get(player);
        OfflinePlayer op = Bukkit.getOfflinePlayer(msg.sender());
        Component sender = !op.hasPlayedBefore() ? Component.text("UNKNOWN SENDER").color(NamedTextColor.RED) : Component.text(op.getName());
        Temporal time = msg.timeAsTemporal();
        infinitum.getI18n().sendMessage("chat.info", player, sender, Global.FORMATTER.format(time), msg.message());
    }
}
