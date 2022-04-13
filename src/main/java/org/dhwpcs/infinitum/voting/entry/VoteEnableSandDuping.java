package org.dhwpcs.infinitum.voting.entry;

import io.github.initauther97.nugget.adventure.text.TextEntry;
import org.dhwpcs.infinitum.Global;
import org.dhwpcs.infinitum.Infinitum;
import org.dhwpcs.infinitum.config.MixinConfig;
import org.dhwpcs.infinitum.voting.InfinitumVoting;

import java.util.function.Consumer;

public class VoteEnableSandDuping implements IVoteEntry {

    private final Infinitum infinitum;

    public VoteEnableSandDuping(Infinitum infinitum) {
        this.infinitum = infinitum;
    }

    @Override
    public String getVoteId() {
        return "exp.sand_duping.enable";
    }

    @Override
    public TextEntry getDescription() {
        return infinitum.getI18n().translate("vote.sand_duping.enable.desc");
    }

    @Override
    public Consumer<InfinitumVoting> succeedAction() {
        return ctx -> MixinConfig.sandDuping = true;
    }
}
