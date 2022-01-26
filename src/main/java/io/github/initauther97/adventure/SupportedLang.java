package io.github.initauther97.adventure;

import org.dhwpcs.infinitum.util.FuzzyFilter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public enum SupportedLang {
    ZH_CN,
    EN_US;

    public static final List<String> NAMES = List.of("ZH_CN", "EN_US");

    public static String[] fuzzy(String input) {
        Set<String> strs = new HashSet<>(2);
        strs.add("ZH_CN");
        strs.add("EN_US");
        FuzzyFilter.fuzzy(strs, input);
        return strs.toArray(String[]::new);
    }

    public static SupportedLang getIfExists(String lang) {
        return switch (lang) {
            case "ZH_CN" -> ZH_CN;
            case "EN_US" -> EN_US;
            default -> null;
        };
    }
}
