package org.dhwpcs.infinitum.voting.entry;

import io.github.initauther97.adventure.text.TextEntry;
import org.dhwpcs.infinitum.Constants;
import org.dhwpcs.infinitum.GlobalConfig;

public class VoteCorrectTntAccel implements IVoteEntry {
    @Override
    public String getVoteId() {
        return "exp.correct_tnt_accel.enable";
    }

    @Override
    public TextEntry getDescription() {
        return (TextEntry) Constants.TEXTS.get("vote.entry.correct_tnt_accel.desc");
    }

    @Override
    public Runnable succeedAction() {
        return () -> GlobalConfig.fixExplosion = true;
    }
}
