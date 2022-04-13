package io.github.initauther97.nugget.command.arguments.brigadier;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.github.initauther97.nugget.command.arguments.EnumFindResult;
import net.minecraft.commands.SharedSuggestionProvider;

import java.util.Locale;
import java.util.concurrent.CompletableFuture;

public class EnumArgumentB<E extends Enum<E>> implements ArgumentType<EnumFindResult<E>> {

    private final Class<E> eclz;
    private final String[] allNames;
    public EnumArgumentB(Class<E> eclz) {
        this.eclz = eclz;
        E[] cs = eclz.getEnumConstants();
        this.allNames = new String[cs.length];
        for(int i = 0; i < cs.length; i++) {
            allNames[i] = cs[i].name().toLowerCase(Locale.ROOT);
        }
    }

    @Override
    public EnumFindResult<E> parse(StringReader stringReader) {
        return EnumFindResult.findEnum(eclz, stringReader.readUnquotedString());
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return SharedSuggestionProvider.suggest(allNames, builder);
    }
}
