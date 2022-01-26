package io.github.initauther97.adventure;

public interface AdventureObject<T> {
    T get(SupportedLang lang, Object... args);
}
