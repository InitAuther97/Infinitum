package org.dhwpcs.infinitum.command;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.executors.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.dhwpcs.infinitum.Constants;

import static org.dhwpcs.infinitum.Constants.TEXTS;

public class CommandAcknowledge implements CommandExecutor {

    public static CommandAPICommand create() {
        return new CommandAPICommand("acknowledge")
                .withShortDescription("Check the acknowledge list")
                .executes(new CommandAcknowledge());
    }

    @Override
    public void run(CommandSender sender, Object[] objects) {
        sender.sendMessage(TEXTS.format("command.acknowledge", sender, Constants.getVersion()));
    }
}
