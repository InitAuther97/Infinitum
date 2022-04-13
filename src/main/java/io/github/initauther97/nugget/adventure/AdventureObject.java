package io.github.initauther97.nugget.adventure;

public interface AdventureObject<T> {
    T get(SupportedLang lang, Object... args);
}
