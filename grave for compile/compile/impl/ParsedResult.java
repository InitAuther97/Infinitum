package org.dhwpcs.infinitum.chat.compile.impl;

import net.kyori.adventure.text.Component;
import org.dhwpcs.infinitum.chat.compile.Compilable;
import org.dhwpcs.infinitum.chat.compile.CompilableSection;
import org.dhwpcs.infinitum.chat.compile.CompilationResult;

public record ParsedResult<T>(Compilable<T> compilable, T result)
        implements CompilationResult<T> {
    @Override
    public Compilable<T> type() {
        return compilable;
    }

    @Override
    public T unwrap() {
        return result;
    }

    @Override
    public Exception failed() {
        return null;
    }

    public Component serialize() {
        return compilable.serialize(this);
    }

    public static <T> ParsedResult<T> wrap(CompilationResult<T> result) {
        return new ParsedResult<>(result.type(), result.unwrap());
    }
}
