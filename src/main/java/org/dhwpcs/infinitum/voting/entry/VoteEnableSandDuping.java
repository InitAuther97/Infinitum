package org.dhwpcs.infinitum.voting.entry;

import io.github.initauther97.ialib.adventure.text.TextEntry;
import org.dhwpcs.infinitum.I18n;
import org.dhwpcs.infinitum.Global;

public class VoteEnableSandDuping implements IVoteEntry {
    @Override
    public String getVoteId() {
        return "exp.sand_duping.enable";
    }

    @Override
    public TextEntry getDescription() {
        return I18n.translate("vote.sand_duping.enable.desc");
    }

    @Override
    public Runnable succeedAction() {
        return () -> Global.sandDuping = true;
    }
}
