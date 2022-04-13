package org.dhwpcs.infinitum.chat.compile.tokenize;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;

public record BracketTokenizer(char begin, char end) implements StringTokenizer<> {
    @Override
    public Set<TokenizeSection> tokenize(String raw) {
        Set<TokenizeSection> sections = new HashSet<>();
        Deque<Integer> begins = new ArrayDeque<>();
        boolean escape;
        for(int i = 0; i < raw.length(); i++) {
            if(raw.charAt(i) == begin) {
                begins.push(i);
            }
            if(raw.charAt(i) == end) {
                if(!begins.isEmpty()) {
                    sections.add(TokenizeSection.create(raw, begins.pop()+1, i-1));
                }
            }
        }
        return sections;
    }
}
