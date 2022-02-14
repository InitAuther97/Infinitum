package org.dhwpcs.infinitum.voting.entry;

import io.github.initauther97.ialib.adventure.text.TextEntry;
import org.dhwpcs.infinitum.I18n;
import org.dhwpcs.infinitum.Global;

public class VoteDisableSandDuping implements IVoteEntry {
    @Override
    public String getVoteId() {
        return "exp.sand_duping.disable";
    }

    @Override
    public TextEntry getDescription() {
        return I18n.translate("vote.sand_duping.disable.desc");
    }

    @Override
    public Runnable succeedAction() {
        return () -> Global.sandDuping = false;
    }
}
