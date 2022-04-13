package org.dhwpcs.infinitum.chat.compile;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.dhwpcs.infinitum.chat.compile.tokenize.StringTokenizer;

import java.util.Map;

public interface Compilable<T> {
    StringTokenizer tokenizer();
    Map<String, ?> extractMeta(CompilableSection<T> result, CompilationContext ctx);
    CompilationResult<T> compile(Map<String, ?> meta, CompilationContext ctx);
    String key();
    default Component serialize(CompilationResult<T> result) {
        return result.failed() == null
                ? Component.text(result.unwrap().toString())
                : Component.text(result.failed().getLocalizedMessage()).color(NamedTextColor.RED);
    }
}
