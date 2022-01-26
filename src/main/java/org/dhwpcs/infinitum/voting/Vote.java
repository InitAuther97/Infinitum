package org.dhwpcs.infinitum.voting;

import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.SetMultimap;
import org.dhwpcs.infinitum.GlobalConfig;
import org.dhwpcs.infinitum.voting.callback.IVoteCallback;
import org.dhwpcs.infinitum.voting.entry.IVoteEntry;

import java.util.Set;
import java.util.UUID;

public class Vote {
    int remaining;
    int vote;
    IVoteEntry entry;
    Set<IVoteCallback> hooks;
    boolean finished;
    boolean opDispose = false;
    UUID uid = UUID.randomUUID();
    SetMultimap<VoteType, UUID> participants = MultimapBuilder.enumKeys(VoteType.class).hashSetValues().build();

    public Vote(IVoteEntry entry, int period, Set<IVoteCallback> hooks) {
        this.entry = entry;
        remaining = period;
        vote = 0;
        this.hooks =Set.copyOf(hooks);
    }

    public VoteParticipatingResult vote(VoteType type, UUID id, boolean isOp) {
        if(participants.put(type, id)) {
            if(!opDispose) {
                vote= switch (type) {
                    case ACCEPT -> vote + 1;
                    case REJECT -> isOp && GlobalConfig.voteOpAbsoluteDisagreement ? -2147483648 : vote - 1;
                };
                opDispose = isOp;
            }
            return new VoteParticipatingResult(VoteParticipatingResultType.SUCCESS);
        }
        return new VoteParticipatingResult(VoteParticipatingResultType.ALREADY_VOTED);
    }

    public boolean isFinished() {
        return finished;
    }

    public boolean isDisposedByOp() {
        return opDispose;
    }

    public Set<UUID> getParticipants(VoteType type) {
        return participants.get(type);
    }

    public IVoteEntry getEntry() {
        return entry;
    }

    public  void tick() {
        if(remaining <= 0) {
            if(vote > 0) {
                entry.succeedAction().run();
                hooks.forEach(it -> it.voteEnded(this, VoteResult.ACCEPT));
            } else {
                hooks.forEach(it -> it.voteEnded(this, VoteResult.DENY));
            }
        } else remaining--;
    }

    public UUID getUid() {
        return uid;
    }
}
