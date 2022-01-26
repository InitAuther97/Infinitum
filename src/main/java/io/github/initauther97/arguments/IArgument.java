package io.github.initauther97.arguments;

import io.github.initauther97.arguments.env.Environment;

import java.util.function.BiFunction;
import java.util.function.Function;

public interface IArgument<T> {

    Class<T> getArgumentType();

    T parse(String arg, Environment env) throws Exception;

    default T getDefaultValue() {
        return null;
    }

    static<T> IArgument<T> create(Class<T> argType, Function<String, T> parser) {
        return new IArgument<>() {
            @Override
            public Class<T> getArgumentType() {
                return argType;
            }

            @Override
            public T parse(String arg, Environment env) {
                return parser.apply(arg);
            }
        };
    }

    static<T> IArgument<T> create(Class<T> argType, BiFunction<String, Environment, T> parser, Function<Environment, Boolean> checker) {
        return new IArgument<>() {
            @Override
            public Class<T> getArgumentType() {
                return argType;
            }

            @Override
            public T parse(String arg, Environment env) {
                return parser.apply(arg, env);
            }

            @Override
            public boolean checkCompatible(Environment env) {
                return checker.apply(env);
            }
        };
    }

    static<T> IArgument<T> withDefault(IArgument<T> template, T def) {
        return new IArgument<>() {
            @Override
            public Class<T> getArgumentType() {
                return template.getArgumentType();
            }

            @Override
            public T parse(String arg, Environment env) throws Exception {
                return template.parse(arg, env);
            }

            @Override
            public T getDefaultValue() {
                return def;
            }

            @Override
            public boolean checkCompatible(Environment env) {
                return template.checkCompatible(env);
            }
        };
    }

    default boolean checkCompatible(Environment env) {
        return true;
    }
}
