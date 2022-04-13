package org.dhwpcs.infinitum.voting.entry;

import io.github.initauther97.nugget.adventure.text.TextEntry;
import org.dhwpcs.infinitum.voting.InfinitumVoting;

import java.util.function.Consumer;

public interface IVoteEntry {
    String getVoteId();
    TextEntry getDescription();
    Consumer<InfinitumVoting> succeedAction();
}
