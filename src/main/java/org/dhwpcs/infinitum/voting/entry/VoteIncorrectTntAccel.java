package org.dhwpcs.infinitum.voting.entry;

import io.github.initauther97.nugget.adventure.text.TextEntry;
import org.dhwpcs.infinitum.config.MixinConfig;
import org.dhwpcs.infinitum.Infinitum;
import org.dhwpcs.infinitum.voting.InfinitumVoting;

import java.util.function.Consumer;

public class VoteIncorrectTntAccel implements IVoteEntry {
    private final Infinitum infinitum;

    public VoteIncorrectTntAccel(Infinitum infinitum) {

        this.infinitum = infinitum;
    }

    @Override
    public String getVoteId() {
        return "exp.correct_tnt_accel.disable";
    }

    @Override
    public TextEntry getDescription() {
        return infinitum.getI18n().translate("vote.tnt_accel.disable.desc");
    }

    @Override
    public Consumer<InfinitumVoting> succeedAction() {
        return ctx -> MixinConfig.fixExplosion = false;
    }
}
