package io.github.initauther97.nugget.command.suggestion;

import dev.jorel.commandapi.IStringTooltip;
import dev.jorel.commandapi.StringTooltip;
import dev.jorel.commandapi.SuggestionInfo;
import org.bukkit.NamespacedKey;
import org.bukkit.World;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface Suggestor extends Function<SuggestionInfo, IStringTooltip[]> {

    IStringTooltip[] EMPTY_TT = new IStringTooltip[0];

    @Override
    default IStringTooltip[] apply(SuggestionInfo suggestionInfo) {
        return suggest(suggestionInfo).toArray(EMPTY_TT);
    }

    List<IStringTooltip> suggest(SuggestionInfo info);

    static Suggestor suggest(String... suggestions) {
        List<IStringTooltip> tts = Arrays.stream(suggestions).map(StringTooltip::none).collect(Collectors.toList());
        return unused -> tts;
    }

    static Suggestor suggestWorldKeys() {
        return i -> i.sender().getServer().getWorlds()
                .stream()
                .map(World::getKey)
                .map(NamespacedKey::toString)
                .map(StringTooltip::none)
                .collect(Collectors.toList());
    }
}
