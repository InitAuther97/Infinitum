package io.github.initauther97.arguments.env;

public class DuplicateCharacteristicsException extends RuntimeException {
    public DuplicateCharacteristicsException() {
    }

    public DuplicateCharacteristicsException(String message) {
        super(message);
    }

    public DuplicateCharacteristicsException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateCharacteristicsException(Throwable cause) {
        super(cause);
    }
}
