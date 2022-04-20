package io.github.initauther97.nugget.adventure.text;

import io.github.initauther97.nugget.adventure.SupportedLang;
import io.github.initauther97.nugget.adventure.util.ComponentUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.Style;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record FormatTextEntry(TextEntry raw, List<TextEntry> entries) implements TextEntry {
    private static final Object MARKER = new Object(){};
    private static final Pattern ARGUMENT = Pattern.compile("\\\\{0}\\{}");
    @Override
    public TextComponent get(SupportedLang lang, Object... args) {
        TextComponent component = raw.get(lang, args);
        Style style = component.style();
        String raw = ComponentUtils.extractContent(component);
        List<Component> parsed = new LinkedList<>();
        /*{
            char[] chars = raw.toCharArray();
            boolean escape = false;
            StringRange range = new StringRange(0);
            Iterator<TextEntry> iter = entries.iterator();
            for (int i = 0; i < chars.length; i++) {
                if (i + 1 < chars.length) {
                    if (chars[i] == '\\' && (chars[i + 1] == '{')) {
                        chars[i] = ' ';
                        escape = true;
                    } else if (!escape && chars[i] == '{' && chars[i + 1] == '}') {
                        range.setEnd(i);
                        parsed.add(Component.text(range.<String>transform(raw::substring)).style(style));
                        parsed.add(iter.next().get(lang, args));
                        range.setBegin(i + 2);
                    } else {
                        escape = false;
                    }
                }
            }
        }*/
        Matcher matcher = ARGUMENT.matcher(raw);
        MatchResult[] results = matcher.results().toArray(MatchResult[]::new);
        StringBuilder sb = new StringBuilder();
        int begin = 0;
        for(int i = 0; i < results.length; i++) {
            sb.append(raw, begin, results[i].start());
            sb.append(entries.get(i).get(lang, args).content());
            begin = results[i].end();
        }
        if(begin < raw.length()) {
            sb.append(raw.substring(begin));
        }
        return Component.text(sb.toString()).style(component.style());
    }
}
