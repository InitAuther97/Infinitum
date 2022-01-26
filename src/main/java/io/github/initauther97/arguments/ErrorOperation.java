package io.github.initauther97.arguments;

public enum ErrorOperation {
    /**
     * Skip the failed element, filling it with null
     */
    SKIP,
    /**
     * Throw an exception and return.
     */
    TERMINATE,
    /**
     * Has the same effect with {@link #SKIP}, but
     * indicating the error has been handled by
     * the handler.
     */
    DUMMY
}
