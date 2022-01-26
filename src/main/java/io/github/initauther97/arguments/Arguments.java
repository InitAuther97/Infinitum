package io.github.initauther97.arguments;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;

public interface Arguments {
    IArgument<Boolean> BOOLEAN = IArgument.create(Boolean.TYPE, Boolean::valueOf);
    IArgument<Integer> INTEGER = IArgument.create(Integer.TYPE, Integer::valueOf);
    IArgument<Long> LONG = IArgument.create(Long.TYPE, Long::valueOf);
    IArgument<String> STRING = IArgument.create(String.class, Function.identity());
    IArgument<UUID> UUID = IArgument.create(UUID.class, java.util.UUID::fromString);
    IArgument<List> VAR_STRING = IArgument.create(List.class,
            (str, env) -> List.of(str.split(env.get("str_spliterator").getAs(String.class))),
            env -> env.has("str_spliterator"));

    static<T extends Enum<T>> IArgument<T> ofEnum(Class<T> enumType) {
        return IArgument.create(enumType, val -> Enum.valueOf(enumType, val));
    }
}
