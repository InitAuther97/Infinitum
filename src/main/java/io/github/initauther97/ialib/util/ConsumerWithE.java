package io.github.initauther97.ialib.util;

public interface ConsumerWithE<T, E extends Exception> {
    void accept(T it) throws E;
}
