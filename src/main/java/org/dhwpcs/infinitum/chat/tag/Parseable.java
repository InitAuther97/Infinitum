package org.dhwpcs.infinitum.chat.tag;

import org.dhwpcs.infinitum.chat.adventure.Translatable;

public interface Parseable {
    Translatable parse(ChatContext ctx);
    String raw();

    static Parseable plain(String plain) {
        return new Parseable() {
            @Override
            public Translatable parse(ChatContext ctx) {
                return Translatable.plain(plain);
            }

            @Override
            public String raw() {
                return plain;
            }
        };
    }

    static Parseable compose(Parseable... ps) {
        return new ComplexParseable(ps);
    }
}
