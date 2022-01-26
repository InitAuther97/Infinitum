package io.github.initauther97.adventure.serial;

import java.util.function.Function;

public interface Deserializer<I,O> {
    O deserialize(I value, DeserializationContext context);

    default<T> Deserializer<I,T> andThen(Deserializer<O,T> next) {
        return complex(this, next);
    }

    static<E extends Enum<E>> Deserializer<String, E> deserializeEnum(Class<E> enumClass) {
        return (value, context) -> Enum.valueOf(enumClass, value);
    }

    static<I,T> Deserializer<I,T> function(Function<I,T> constructor) {
        return ((value, context) -> constructor.apply(value));
    }

    static<I,U,T> Deserializer<I,T> complex(Deserializer<I,U> first, Deserializer<U,T> next) {
        return (i,ctx) -> next.deserialize(first.deserialize(i, ctx), ctx);
    }

    static<I,U,T> Deserializer<I,T> complex(Deserializer<I,U> first, Function<U,T> next) {
        return complex(first, function(next));
    }

    static<I,U,T> Deserializer<I,T> complexFunction(Function<I,U> first, Function<U,T> next) {
        return function(first.andThen(next));
    }
}
