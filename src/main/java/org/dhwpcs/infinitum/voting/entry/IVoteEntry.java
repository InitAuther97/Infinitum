package org.dhwpcs.infinitum.voting.entry;

import io.github.initauther97.adventure.text.TextEntry;

public interface IVoteEntry {
    String getVoteId();
    TextEntry getDescription();
    Runnable succeedAction();
}
