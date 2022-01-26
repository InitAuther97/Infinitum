package org.dhwpcs.infinitum.util;

import java.util.Collection;
import java.util.function.IntFunction;

public class FuzzyFilter {
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
        for(String t : strs) {
            cloned.add(t);
        }
        fuzzy(cloned, provided);
        return cloned;
    }
}
