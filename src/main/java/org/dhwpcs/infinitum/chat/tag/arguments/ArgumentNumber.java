package org.dhwpcs.infinitum.chat.tag.arguments;

import org.dhwpcs.infinitum.chat.ChatContext;
import org.dhwpcs.infinitum.chat.tag.parse.TagFailedException;
import org.dhwpcs.infinitum.chat.tag.parse.argument.TagContext;

import java.util.function.Function;

public class ArgumentNumber<T extends Number> implements Argument<T> {

    private final Class<T> valueClass;
    private final Function<String, T> function;

    ArgumentNumber(Class<T> valueClass, Function<String, T> function) {
        this.valueClass = valueClass;
        this.function = function;
    }

    @Override
    public Class<T> getValueClass() {
        return valueClass;
    }

    @Override
    public T parse(Object input, ChatContext ctx) {
        return function.apply(input.toString());
    }


    public static ArgumentNumber<Byte> bytes() {
        return new ArgumentNumber<>(Byte.class, Byte::valueOf);
    }

    public static ArgumentNumber<Short> shorts() {
        return new ArgumentNumber<>(Short.class, Short::valueOf);
    }

    public static ArgumentNumber<Integer> integers() {
        return new ArgumentNumber<>(Integer.class, Integer::valueOf);
    }

    public static ArgumentNumber<Long> longs() {
        return new ArgumentNumber<>(Long.class, Long::valueOf);
    }

    public static ArgumentNumber<Float> floats() {
        return new ArgumentNumber<>(Float.class, Float::valueOf);
    }

    public static ArgumentNumber<Double> doubles() {
        return new ArgumentNumber<>(Double.class, Double::valueOf);
    }

    public static<E extends Number> E getNumber(String id, Class<E> clz, TagContext ctx) throws TagFailedException {
        return ctx.getArgument(id, clz);
    }
}
