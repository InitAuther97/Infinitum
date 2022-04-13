package org.dhwpcs.infinitum.chat.compile.impl;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.dhwpcs.infinitum.chat.compile.Compilable;
import org.dhwpcs.infinitum.chat.compile.CompilationResult;

public record ComplexParsedResult(String raw, ParsedResult<?>[] results)
    implements CompilationResult<Component> {

    @Override
    public Compilable<Component> type() {
        return null;
    }

    @Override
    public Component unwrap() {
        return raw.transform(it -> {
            int lastEnd = 0;
            TextComponent.Builder builder = Component.text();
            for(ParsedResult<?> cr : results) {
                builder.append(Component.text(it.substring(lastEnd, cr.region().start())));
                builder.append(cr.serialize());
                lastEnd = cr.region().end();
            }
            return builder.build();
        });
    }

    @Override
    public Exception failed() {
        return null;
    }
}
