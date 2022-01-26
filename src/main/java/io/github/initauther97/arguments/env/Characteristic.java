package io.github.initauther97.arguments.env;

public interface Characteristic<T> {
    String identifier();
    T get();
    default<V> V getAs(Class<V> clz) {
        return clz.isInstance(get()) ? (V)get() : null;
    }
}
