package org.dhwpcs.infinitum.chat.compile.tokenize;

import org.dhwpcs.infinitum.chat.compile.Compilable;
import org.dhwpcs.infinitum.chat.compile.CompilableSection;

public record TokenizeSection(String raw, int begin, int end) {
    public static TokenizeSection create(String raw, int begin, int end) {
        return new TokenizeSection(raw, begin, end);
    }

    public<T> CompilableSection<T> withCompilable(Compilable<T> compilable) {
        return new CompilableSection<>(compilable, begin, end);
    }
}
