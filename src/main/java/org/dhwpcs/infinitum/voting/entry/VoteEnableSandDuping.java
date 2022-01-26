package org.dhwpcs.infinitum.voting.entry;

import io.github.initauther97.adventure.text.TextEntry;
import org.dhwpcs.infinitum.Constants;
import org.dhwpcs.infinitum.GlobalConfig;

public class VoteEnableSandDuping implements IVoteEntry {
    @Override
    public String getVoteId() {
        return "exp.sand_duping.enable";
    }

    @Override
    public TextEntry getDescription() {
        return Constants.TEXTS.translate("vote.sand_duping.enable.desc");
    }

    @Override
    public Runnable succeedAction() {
        return () -> GlobalConfig.sandDuping = true;
    }
}
