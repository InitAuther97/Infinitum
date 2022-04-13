package io.github.initauther97.nugget.util;

import java.util.Collection;
import java.util.function.IntFunction;

public class Fuzzy {
    public static void fuzzy(Collection<String> strs, String provided) {
        for(int i = 0; i < provided.length(); i++) {
            int finalI = i;
            strs.removeIf(str -> finalI > str.length() || str.charAt(finalI) != provided.charAt(finalI));
            if(strs.isEmpty()) {
                break;
            }
        }
    }

    public static<T extends Collection<String>> T fuzzyCloned(T strs, String provided, IntFunction<T> constr) {
        T cloned = constr.apply(strs.size());
        cloned.addAll(strs);
        fuzzy(cloned, provided);
        return cloned;
    }
}
