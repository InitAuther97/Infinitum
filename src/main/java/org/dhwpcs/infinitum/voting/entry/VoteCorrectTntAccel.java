package org.dhwpcs.infinitum.voting.entry;

import io.github.initauther97.nugget.adventure.text.TextEntry;
import org.dhwpcs.infinitum.config.MixinConfig;
import org.dhwpcs.infinitum.Infinitum;
import org.dhwpcs.infinitum.voting.InfinitumVoting;

import java.util.function.Consumer;

public class VoteCorrectTntAccel implements IVoteEntry {
    private final Infinitum infinitum;

    public VoteCorrectTntAccel(Infinitum infinitum) {
        this.infinitum = infinitum;
    }

    @Override
    public String getVoteId() {
        return "exp.correct_tnt_accel.enable";
    }

    @Override
    public TextEntry getDescription() {
        return infinitum.getI18n().translate("vote.entry.correct_tnt_accel.desc");
    }

    @Override
    public Consumer<InfinitumVoting> succeedAction() {
        return ctx -> MixinConfig.fixExplosion = true;
    }
}
