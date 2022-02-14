package io.github.initauther97.ialib.command.arguments;

import java.util.Arrays;

public interface EnumFindResult<E extends Enum<E>> {
    E unwrap();
    Throwable getIfFailed();
    static<E extends Enum<E>> EnumFindResult<E> findEnum(Class<E> eclz, String id) {
        try {
            E res = Arrays.stream(eclz.getEnumConstants())
                    .filter(it -> it.name().equalsIgnoreCase(id))
                    .findAny().orElseThrow();
            return new EnumFindResult<E>() {
                @Override
                public E unwrap() {
                    return res;
                }

                @Override
                public Throwable getIfFailed() {
                    return null;
                }
            };
        } catch (Throwable e) {
            return new EnumFindResult<E>() {
                @Override
                public E unwrap() {
                    throw new NullPointerException();
                }

                @Override
                public Throwable getIfFailed() {
                    return e;
                }
            };
        }
    }
}
