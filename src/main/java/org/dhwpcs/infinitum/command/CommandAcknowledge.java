package org.dhwpcs.infinitum.command;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.executors.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.dhwpcs.infinitum.Infinitum;


public class CommandAcknowledge implements CommandExecutor {

    private final Infinitum infinitum;

    public CommandAcknowledge(Infinitum infinitum) {

        this.infinitum = infinitum;
    }

    public static CommandAPICommand create(Infinitum infinitum) {
        return new CommandAPICommand("acknowledge")
                .withShortDescription("Check the acknowledge list")
                .executes(new CommandAcknowledge(infinitum));
    }

    @Override
    public void run(CommandSender sender, Object[] objects) {
        infinitum.getI18n().sendMessage("command.acknowledge.1", sender, Infinitum.version());
        infinitum.getI18n().sendMessage("command.acknowledge.2", sender);
        infinitum.getI18n().sendMessage("command.acknowledge.3", sender);
        infinitum.getI18n().sendMessage("command.acknowledge.4", sender);
    }
}
