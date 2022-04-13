package org.dhwpcs.infinitum.chat.compile.impl;

import org.dhwpcs.infinitum.chat.compile.Compilable;
import org.dhwpcs.infinitum.chat.compile.CompilationResult;

public record PlainResult(String plain) implements CompilationResult<String> {

    @Override
    public Compilable<String> type() {
        return Compiler.PLAIN;
    }

    @Override
    public String unwrap() {
        return plain;
    }

    @Override
    public Exception failed() {
        return null;
    }
}
