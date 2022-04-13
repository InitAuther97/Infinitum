package org.dhwpcs.infinitum.chat.compile.impl;

import org.dhwpcs.infinitum.chat.compile.Compilable;
import org.dhwpcs.infinitum.chat.compile.CompilableSection;
import org.dhwpcs.infinitum.chat.compile.CompilationContext;
import org.dhwpcs.infinitum.chat.compile.CompilationResult;
import org.dhwpcs.infinitum.chat.compile.tokenize.SeparatorTokenizer;
import org.dhwpcs.infinitum.chat.compile.tokenize.StringTokenizer;

import java.util.Map;

public record MemoryCompilable(char separator) implements Compilable<Object> {

    @Override
    public StringTokenizer tokenizer() {
        return new SeparatorTokenizer('§');
    }

    @Override
    public Map<String, ?> extractMeta(CompilableSection<Object> result, CompilationContext ctx) {
        return Map.of("value", ctx.getMemoryMirror(result.raw().substring(result.begin(), result.end())));
    }

    @Override
    public CompilationResult<Object> compile(Map<String, ?> meta, CompilationContext ctx) {
        return new ParsedResult<>(this, meta.get("value"));
    }

    @Override
    public String key() {
        return "memory";
    }
}
