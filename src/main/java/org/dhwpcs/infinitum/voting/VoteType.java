package org.dhwpcs.infinitum.voting;

import java.util.Set;

public enum VoteType {
    ACCEPT,
    REJECT;

    public static VoteType fromString(String type) {

        if(ACCEPTANCE.contains(type)) {
            return ACCEPT;
        }

        if(REJECTION.contains(type)) {
            return REJECT;
        }
        return null;
    }

    public static Set<String> ACCEPTANCE = Set.of(
            "accept","agree","yes","affirmative",
            "right","alright","consent","approve",
            "yee","nya","meow"
    );

    public static Set<String> REJECTION = Set.of(
            "deny","disagree","no","object",
            "reject","decline","negative","oppose",
            "nmsl","wcnmnlmm"
    );

    public static Set<String> SUGGESTED_INPUT = Set.of(
            "accept", "reject"
    );
}
