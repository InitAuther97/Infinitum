package org.dhwpcs.infinitum.chat.compile.tokenize;

import java.util.HashSet;
import java.util.Set;

public record SeparatorTokenizer(char separator) implements StringTokenizer {

    @Override
    public Set<TokenizeSection> tokenize(String raw) {
        Set<TokenizeSection> result = new HashSet<>();
        int lastBegin = -1;
        for(int i = 0; i < raw.length(); i++) {
            if(raw.charAt(i) == separator) {
                if(lastBegin < 0) {
                    lastBegin = i;
                } else {
                    result.add(TokenizeSection.create(raw, lastBegin+1, i-1));
                    lastBegin = -1;
                }
            }
        }
        return result;
    }
}
