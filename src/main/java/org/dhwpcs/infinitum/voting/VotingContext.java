package org.dhwpcs.infinitum.voting;

import org.dhwpcs.infinitum.voting.callback.IVoteCallback;
import org.dhwpcs.infinitum.voting.entry.*;

import java.util.*;
import java.util.stream.Collectors;

public class VotingContext {

    public static final Set<IVoteEntry> ENTRIES = new HashSet<>();
    public static final Runnable TICKING_INTERFACE = VotingContext::tick;

    static {
        register(new VoteCorrectTntAccel());
        register(new VoteIncorrectTntAccel());
        register(new VoteEnableSandDuping());
        register(new VoteDisableSandDuping());
    }

    static final Map<String, Vote> votes = new HashMap<>();

    public static void register(IVoteEntry entry) {
        ENTRIES.add(entry);
    }

    public static VoteRequestResult createVote(String string, int duration, IVoteCallback... callbacks) {
        if(votes.containsKey(string)) {
            return new VoteRequestResult(VoteRequestResultType.ALREADY_ONGOING, votes.get(string));
        }

        for(IVoteEntry entry : ENTRIES) {
            if(entry.getVoteId().equals(string)) {
                Vote vote = new Vote(entry, duration, Set.of(callbacks));
                votes.put(string, vote);
                return new VoteRequestResult(VoteRequestResultType.SUCCESS, vote);
            }
        }

        return new VoteRequestResult(VoteRequestResultType.UNKNOWN_VOTE, string);
    }

    public static VoteParticipatingResult vote(String id, UUID voteid, VoteType type, boolean isOp) {
        if(!votes.containsKey(id)) {
            return new VoteParticipatingResult(VoteParticipatingResultType.NO_ONGOING);
        }

        Vote theVote = votes.get(id);
        if(!theVote.uid.equals(voteid)) {
            return new VoteParticipatingResult(VoteParticipatingResultType.NOT_MATCH, theVote.uid, voteid);
        }

        return theVote.vote(type, voteid, isOp);
    }

    public static Optional<Vote> findVote(String id) {
        return Optional.ofNullable(votes.get(id));
    }

    public static Optional<Vote> findVote(UUID id) {
        return votes.values().stream().filter(it -> it.uid.equals(id)).findAny();
    }

    private static void tick() {
        for (Vote vote : votes.values()) {
            vote.tick();
        }
    }

    public static List<String> getVoteIds() {
        return ENTRIES.stream().map(IVoteEntry::getVoteId).collect(Collectors.toList());
    }

    public static List<UUID> getAllOngoings() {
        return votes.values().stream().map(Vote::getUid).toList();
    }
}
