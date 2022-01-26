package org.dhwpcs.infinitum.voting.callback;

import org.dhwpcs.infinitum.voting.Vote;
import org.dhwpcs.infinitum.voting.VoteResult;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public interface IVoteCallback {
    void voteEnded(Vote entry, VoteResult result);

    static IVoteCallback fromUtil(BiConsumer<Vote, VoteResult> consumer) {
        return consumer::accept;
    }

    static IVoteCallback fromUtilOnlyResult(Consumer<VoteResult> consumer) {
        return (unused,result) -> consumer.accept(result);
    }

    static IVoteCallback fromUtilOnlyEntry(Consumer<Vote> consumer) {
        return (entry,unused) -> consumer.accept(entry);
    }

    static IVoteCallback from(Runnable runnable) {
        return (unused,shit) -> runnable.run();
    }
}
