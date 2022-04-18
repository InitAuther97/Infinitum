package org.dhwpcs.infinitum.voting;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.dhwpcs.infinitum.Infinitum;
import org.dhwpcs.infinitum.voting.callback.IVoteCallback;
import org.dhwpcs.infinitum.voting.entry.*;

import java.util.*;

public class InfinitumVoting {

    private final Set<IVoteEntry> entries = new HashSet<>();
    public final Runnable ticking_interface = this::tick;

    private final Map<String, Object> memory = new HashMap<>();
    private final Infinitum infinitum;

    boolean opAbD;

    public InfinitumVoting(Infinitum infinitum){
        this.infinitum = infinitum;
        register(new VoteCorrectTntAccel(infinitum));
        register(new VoteIncorrectTntAccel(infinitum));
        register(new VoteEnableSandDuping(infinitum));
        register(new VoteDisableSandDuping(infinitum));
    }

    public void initialize(ConfigurationSection section) {
        opAbD = section.getBoolean("vote_op_absolute_disagreement",  false);
        Bukkit.getScheduler().runTaskTimer(infinitum, ticking_interface, 0, 0);
    }

    public void write(ConfigurationSection section) {
        section.set("vote_op_absolute_disagreement", opAbD);
    }

    final Map<String, Vote> votes = new HashMap<>();

    public void register(IVoteEntry entry) {
        entries.add(entry);
    }

    public VoteRequestResult createVote(String string, int duration, IVoteCallback... callbacks) {
        if(votes.containsKey(string)) {
            return new VoteRequestResult(VoteRequestResultType.ALREADY_ONGOING, votes.get(string));
        }

        for(IVoteEntry entry : entries) {
            if(entry.getVoteId().equals(string)) {
                Vote vote = new Vote(entry, duration, Set.of(callbacks), opAbD);
                votes.put(string, vote);
                return new VoteRequestResult(VoteRequestResultType.SUCCESS, vote);
            }
        }

        return new VoteRequestResult(VoteRequestResultType.UNKNOWN_VOTE, string);
    }

    public VoteParticipatingResult vote(String id, UUID voteid, VoteType type, boolean isOp) {
        if(!votes.containsKey(id)) {
            return new VoteParticipatingResult(VoteParticipatingResultType.NO_ONGOING);
        }

        Vote theVote = votes.get(id);
        if(!theVote.uid.equals(voteid)) {
            return new VoteParticipatingResult(VoteParticipatingResultType.NOT_MATCH, theVote.uid, voteid);
        }

        return theVote.vote(type, voteid, isOp);
    }

    public Optional<Vote> findVote(String id) {
        return Optional.ofNullable(votes.get(id));
    }

    public Optional<Vote> findVote(UUID id) {
        for(Vote v : votes.values()) {
            if(v.uid.equals(id)) {
                return Optional.of(v);
            }
        }
        return Optional.empty();
    }

    private void tick() {
        Vote vte;
        for(String key : votes.keySet()) {
            vte = votes.get(key);
            vte.tick(this);
            if(vte.finished) {
                votes.remove(key);
            }
        }
    }

    public List<String> getVoteIds() {
        List<String> ids = new ArrayList<>(entries.size());
        for(IVoteEntry entry : entries) {
            ids.add(entry.getVoteId());
        }
        return ids;
    }

    public List<UUID> getAllOngoings() {
        List<UUID> ongoings = new ArrayList<>(votes.size());
        for(Vote vote : votes.values()) {
            ongoings.add(vote.uid);
        }
        return ongoings;
    }
}
