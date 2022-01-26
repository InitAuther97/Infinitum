package io.github.initauther97.adventure.serial;

import io.github.initauther97.adventure.text.FormatTextEntry;
import io.github.initauther97.adventure.text.TextEntry;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DeserializerFormatText extends DeserializerTextEntry<FormatTextEntry> {
    @Override
    public FormatTextEntry deserializeValue(Object value, DeserializationContext ctx) {
        Map<String, Object> map = (Map<String, Object>) value;
        TextEntry raw = Deserializers.textEntry(map.get("raw"), ctx);
        List<TextEntry> values = ((List<Object>) map.get("arguments")).stream()
                .map(o -> Deserializers.textEntry(o, ctx))
                .collect(Collectors.toList());
        return new FormatTextEntry(raw, values);
    }
}
