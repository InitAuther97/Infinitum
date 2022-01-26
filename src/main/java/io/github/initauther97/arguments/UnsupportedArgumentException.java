package io.github.initauther97.arguments;

public class UnsupportedArgumentException extends RuntimeException {
    public UnsupportedArgumentException() {
    }

    public UnsupportedArgumentException(String message) {
        super(message);
    }

    public UnsupportedArgumentException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnsupportedArgumentException(Throwable cause) {
        super(cause);
    }
}
