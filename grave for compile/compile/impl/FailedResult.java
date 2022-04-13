package org.dhwpcs.infinitum.chat.compile.impl;

import org.dhwpcs.infinitum.chat.compile.Compilable;
import org.dhwpcs.infinitum.chat.compile.CompilationException;
import org.dhwpcs.infinitum.chat.compile.CompilationResult;

public record FailedResult<T>(Compilable<T> compilable, Exception reason) implements CompilationResult<T> {
    @Override
    public Compilable<T> type() {
        return compilable;
    }

    @Override
    public T unwrap() {
        throw new IllegalStateException("Trying to unwrap nothing on an corrupted result");
    }

    @Override
    public Exception failed() {
        return reason;
    }

    public static<E> FailedResult<E> failed(Compilable<E> compilable, Type t) {
        return new FailedResult<>(compilable, t.getException());
    }

    enum Type {
        LAPPING_TAGS(new CompilationException("Lapping tags"));

        public CompilationException getException() {
            return e;
        }

        private final CompilationException e;

        Type(CompilationException e) {
            this.e = e;
        }

    }
}
