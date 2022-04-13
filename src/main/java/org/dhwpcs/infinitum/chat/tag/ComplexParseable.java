package org.dhwpcs.infinitum.chat.tag;

import org.dhwpcs.infinitum.chat.adventure.Translatable;

public record ComplexParseable(Parseable[] delegate) implements Parseable {
    @Override
    public Translatable parse(ChatContext ctx) {
        Translatable[] trs = new Translatable[delegate.length];
        for(int i = 0; i < delegate.length; i++) {
            trs[i] = delegate[i].parse(ctx);
        }
        return Translatable.join(trs);
    }

    @Override
    public String raw() {
        StringBuilder sb = new StringBuilder();
        for(Parseable p : delegate) {
            sb.append(p.raw());
        }
        return sb.toString();
    }
}
