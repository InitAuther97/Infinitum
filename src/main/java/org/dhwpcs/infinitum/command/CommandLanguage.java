package org.dhwpcs.infinitum.command;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.StringArgument;
import dev.jorel.commandapi.executors.CommandExecutor;
import io.github.initauther97.nugget.adventure.SupportedLang;
import org.bukkit.command.CommandSender;
import org.dhwpcs.infinitum.Infinitum;

public class CommandLanguage implements CommandExecutor {

    private final Infinitum infinitum;

    public CommandLanguage(Infinitum infinitum) {

        this.infinitum = infinitum;
    }

    public static CommandAPICommand create(Infinitum infinitum) {
        return new CommandAPICommand("language")
                .withArguments(new StringArgument("lang")
                        .replaceSuggestions(ArgumentSuggestions.strings(SupportedLang.NAMES.toArray(String[]::new))))
                .executes(new CommandLanguage(infinitum));
    }

    @Override
    public void run(CommandSender sender, Object[] objects) {
        SupportedLang lang = SupportedLang.getIfExists((String) objects[0]);
        if(lang == null) {
            infinitum.getI18n().sendMessage("command.lang.incorrect_argument", sender, objects[0], SupportedLang.NAMES);
            return;
        }
        infinitum.getI18n().setLanguage(sender, lang);
        infinitum.getI18n().sendMessage("command.lang.success", sender, lang.name());
    }
}
