package org.dhwpcs.infinitum.command;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.StringArgument;
import dev.jorel.commandapi.executors.CommandExecutor;
import io.github.initauther97.adventure.SupportedLang;
import org.bukkit.command.CommandSender;
import org.dhwpcs.infinitum.Constants;
import org.dhwpcs.infinitum.GlobalConfig;

public class CommandLanguage implements CommandExecutor {

    public static CommandAPICommand create() {
        return new CommandAPICommand("language")
                .withArguments(new StringArgument("lang")
                        .replaceSuggestions(i -> SupportedLang.NAMES.toArray(String[]::new)))
                .executes(new CommandLanguage());
    }

    @Override
    public void run(CommandSender sender, Object[] objects) {
        SupportedLang lang = SupportedLang.getIfExists((String) objects[0]);
        if(lang == null) {
            sender.sendMessage(Constants.TEXTS.format("lang.incorrect_argument", sender, objects[0], SupportedLang.NAMES));
            return;
        }
        GlobalConfig.setLanguage(sender, lang);
        sender.sendMessage(Constants.TEXTS.format("lang.success", sender, lang.name()));
    }
}
