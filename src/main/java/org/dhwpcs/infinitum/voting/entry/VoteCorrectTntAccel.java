package org.dhwpcs.infinitum.voting.entry;

import io.github.initauther97.ialib.adventure.text.TextEntry;
import org.dhwpcs.infinitum.I18n;
import org.dhwpcs.infinitum.Global;

public class VoteCorrectTntAccel implements IVoteEntry {
    @Override
    public String getVoteId() {
        return "exp.correct_tnt_accel.enable";
    }

    @Override
    public TextEntry getDescription() {
        return (TextEntry) I18n.get("vote.entry.correct_tnt_accel.desc");
    }

    @Override
    public Runnable succeedAction() {
        return () -> Global.fixExplosion = true;
    }
}
