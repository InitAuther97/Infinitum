package org.dhwpcs.infinitum.chat.compile.tokenize;

import java.util.Set;

public interface StringTokenizer<T> {
    Set<T> tokenize(String raw);
}
