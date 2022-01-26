package org.dhwpcs.infinitum.voting.entry;

import io.github.initauther97.adventure.text.TextEntry;
import org.dhwpcs.infinitum.Constants;
import org.dhwpcs.infinitum.GlobalConfig;

public class VoteDisableSandDuping implements IVoteEntry {
    @Override
    public String getVoteId() {
        return "exp.sand_duping.disable";
    }

    @Override
    public TextEntry getDescription() {
        return Constants.TEXTS.translate("vote.sand_duping.disable.desc");
    }

    @Override
    public Runnable succeedAction() {
        return () -> GlobalConfig.sandDuping = false;
    }
}
