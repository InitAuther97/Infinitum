package org.dhwpcs.infinitum.command;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.executors.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.dhwpcs.infinitum.I18n;
import org.dhwpcs.infinitum.papershelled.Infinitum;


public class CommandAcknowledge implements CommandExecutor {

    public static CommandAPICommand create() {
        return new CommandAPICommand("acknowledge")
                .withShortDescription("Check the acknowledge list")
                .executes(new CommandAcknowledge());
    }

    @Override
    public void run(CommandSender sender, Object[] objects) {
        I18n.sendMessage("command.acknowledge", sender, Infinitum.version());
    }
}
