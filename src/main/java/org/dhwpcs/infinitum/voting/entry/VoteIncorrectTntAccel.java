package org.dhwpcs.infinitum.voting.entry;

import io.github.initauther97.ialib.adventure.text.TextEntry;
import org.dhwpcs.infinitum.I18n;
import org.dhwpcs.infinitum.Global;

public class VoteIncorrectTntAccel implements IVoteEntry {
    @Override
    public String getVoteId() {
        return "exp.correct_tnt_accel.disable";
    }

    @Override
    public TextEntry getDescription() {
        return I18n.translate("vote.tnt_accel.disable.desc");
    }

    @Override
    public Runnable succeedAction() {
        return () -> Global.fixExplosion = false;
    }
}
