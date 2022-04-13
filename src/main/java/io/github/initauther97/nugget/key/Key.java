package io.github.initauther97.nugget.key;

import java.util.Objects;

public interface Key<T> {
    Class<T> runtimeType();
    boolean isInstance(Object o);
    T cast(Object o);

    static<T> Key<T> make(Class<T> key) {
        Objects.requireNonNull(key);
        return new Key<>() {
            @Override
            public Class<T> runtimeType() {
                return key;
            }

            @Override
            public boolean isInstance(Object o) {
                return key.isInstance(o);
            }

            @Override
            public T cast(Object o) {
                return key.cast(o);
            }
        };
    }
}
