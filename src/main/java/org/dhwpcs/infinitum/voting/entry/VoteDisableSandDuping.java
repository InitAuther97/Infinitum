package org.dhwpcs.infinitum.voting.entry;

import io.github.initauther97.nugget.adventure.text.TextEntry;
import org.dhwpcs.infinitum.Global;
import org.dhwpcs.infinitum.Infinitum;
import org.dhwpcs.infinitum.config.MixinConfig;
import org.dhwpcs.infinitum.voting.InfinitumVoting;

import java.util.function.Consumer;

public class VoteDisableSandDuping implements IVoteEntry {

    private final Infinitum infinitum;

    public VoteDisableSandDuping(Infinitum infinitum) {
        this.infinitum = infinitum;
    }

    @Override
    public String getVoteId() {
        return "exp.sand_duping.disable";
    }

    @Override
    public TextEntry getDescription() {
        return infinitum.getI18n().translate("vote.disable_sand_duping.desc");
    }

    @Override
    public Consumer<InfinitumVoting> succeedAction() {
        return ctx -> MixinConfig.sandDuping = false;
    }
}
