package org.dhwpcs.infinitum.command.config;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandPermission;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import dev.jorel.commandapi.arguments.TextArgument;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.executors.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.dhwpcs.infinitum.Infinitum;
import org.dhwpcs.infinitum.config.MixinConfig;

public class CommandConfig implements CommandExecutor {
    private final Infinitum infinitum;

    public CommandConfig(Infinitum infinitum) {
        this.infinitum = infinitum;
    }

    public static CommandAPICommand create(Infinitum infinitum) {
        return new CommandAPICommand("config")
                .withShortDescription("Configure Infinitum")
                .withArguments(
                        new TextArgument("entry")
                                .replaceSuggestions(ArgumentSuggestions.strings(MixinConfig.APPLIERS.keySet().toArray(String[]::new))),
                        new GreedyStringArgument("value")
                ).withPermission(CommandPermission.OP)
                .executes(new CommandConfig(infinitum));
    }

    @Override
    public void run(CommandSender commandSender, Object[] objects) throws WrapperCommandSyntaxException {
        String entry = (String) objects[0];
        String value = (String) objects[1];
        if(!MixinConfig.has(entry)) {
            infinitum.getI18n().sendMessage("command.config.undefined", commandSender, entry);
        }
        MixinConfig.set(entry, value);
        infinitum.getI18n().sendMessage("command.config.success", commandSender, entry, value);
    }
}
