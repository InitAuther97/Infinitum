package io.github.initauther97.nugget.command.suggestion;

import dev.jorel.commandapi.IStringTooltip;
import dev.jorel.commandapi.StringTooltip;
import dev.jorel.commandapi.SuggestionInfo;

import java.util.ArrayList;
import java.util.List;

public class ListSuggestor implements Suggestor {

    private final List<IStringTooltip> tts;

    public ListSuggestor(List<IStringTooltip> tts) {
        this.tts = new ArrayList<>(tts.size());
        this.tts.addAll(tts);
    }

    public ListSuggestor(String... init) {
        this.tts = new ArrayList<>(init.length);
        for(String i : init) {
            tts.add(StringTooltip.none(i));
        }
    }

    @Override
    public List<IStringTooltip> suggest(SuggestionInfo info) {
        return null;
    }
}
