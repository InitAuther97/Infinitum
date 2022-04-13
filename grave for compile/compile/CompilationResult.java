package org.dhwpcs.infinitum.chat.compile;

public interface CompilationResult<T> {
    Compilable<T> type();
    T unwrap();
    Exception failed();
}
