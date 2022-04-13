package io.github.initauther97.nugget.util;

public interface ConsumerWithE<T, E extends Exception> {
    void accept(T it) throws E;
}
