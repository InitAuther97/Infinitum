package org.dhwpcs.infinitum.voting.entry;

import io.github.initauther97.adventure.text.TextEntry;
import org.dhwpcs.infinitum.Constants;
import org.dhwpcs.infinitum.GlobalConfig;

public class VoteIncorrectTntAccel implements IVoteEntry {
    @Override
    public String getVoteId() {
        return "exp.correct_tnt_accel.disable";
    }

    @Override
    public TextEntry getDescription() {
        return Constants.TEXTS.translate("vote.tnt_accel.disable.desc");
    }

    @Override
    public Runnable succeedAction() {
        return () -> GlobalConfig.fixExplosion = false;
    }
}
