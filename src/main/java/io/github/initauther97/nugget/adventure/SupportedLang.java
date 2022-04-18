package io.github.initauther97.nugget.adventure;

import io.github.initauther97.nugget.util.Fuzzy;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public enum SupportedLang {
    ZH_CN,
    EN_US;

    public static final List<String> NAMES = List.of("ZH_CN", "EN_US");

    public static String[] fuzzy(String input) {
        Set<String> strs = new HashSet<>(2);
        strs.add("ZH_CN");
        strs.add("EN_US");
        Fuzzy.fuzzy(strs, input);
        return strs.toArray(String[]::new);
    }

    public static SupportedLang getIfExists(String lang) {
        return switch (lang) {
            case "ZH_CN" -> ZH_CN;
            case "EN_US" -> EN_US;
            default -> null;
        };
    }

    public static SupportedLang getOrDefault(String lang, SupportedLang def) {
        if(lang != null) {
            return switch (lang) {
                case "ZH_CN" -> ZH_CN;
                case "EN_US" -> EN_US;
                default -> def;
            };
        } else return EN_US;
    }
}
