package io.github.initauther97.ialib.adventure.text.format;

import io.github.initauther97.ialib.adventure.AdventureObject;
import io.github.initauther97.ialib.adventure.SupportedLang;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.util.HSVLike;

import java.util.List;
import java.util.Map;

public record TextColor(Object value) implements AdventureObject<net.kyori.adventure.text.format.TextColor> {
    @Override
    public net.kyori.adventure.text.format.TextColor get(SupportedLang lang, Object... args) {
        if(value instanceof Integer) {
            return net.kyori.adventure.text.format.TextColor.color((Integer) value);
        } else if(value instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) value;
            boolean isRGB = map.containsKey("rgb");
            boolean isHSV = map.containsKey("hsv");
            boolean isNamed = map.containsKey("name");
            if(isRGB && isHSV || isNamed && isRGB || isNamed && isHSV) {
                throw new IllegalArgumentException();
            }

            if(isRGB) {
                List<Integer> res = (List<Integer>) map.get("rgb");
                return net.kyori.adventure.text.format.TextColor.color(res.get(0), res.get(1), res.get(2));
            } else if(isHSV) {
                List<Float> res = (List<Float>) map.get("hsv");
                return net.kyori.adventure.text.format.TextColor.color(HSVLike.of(res.get(0), res.get(1), res.get(2)));
            } else if(isNamed) {
                return NamedTextColor.NAMES.value((String) map.get("name"));
            }

            throw new IllegalArgumentException();
        } else return NamedTextColor.NAMES.value(value.toString());
    }
}
