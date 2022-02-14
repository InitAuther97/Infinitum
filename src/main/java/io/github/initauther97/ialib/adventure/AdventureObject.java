package io.github.initauther97.ialib.adventure;

public interface AdventureObject<T> {
    T get(SupportedLang lang, Object... args);
}
