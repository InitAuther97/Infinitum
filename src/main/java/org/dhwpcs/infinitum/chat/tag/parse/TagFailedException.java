package org.dhwpcs.infinitum.chat.tag.parse;

public class TagFailedException extends Exception {
    private final Reason reason;

    public TagFailedException(Reason reason) {
        this.reason = reason;
    }

    public TagFailedException(Reason reason, String message) {
        super(String.format("At %s:%s", reason, message));
        this.reason = reason;
    }

    public TagFailedException(Reason reason, String message, Throwable cause) {
        super(String.format("At %s:%s", reason, message), cause);
        this.reason = reason;
    }

    public TagFailedException(Reason reason, Throwable cause) {
        super(String.format("At %s", reason), cause);
        this.reason = reason;
    }

    public enum Reason {
        PARSER_UNCLOSED_TAG,
        PARSER_INVALID_EQUAL,
        PARSER_UNDEFINED_TAG,
        CONTEXT_UNDEFINED_ARGUMENT,
        CONTEXT_WRONG_ARGUMENT_TYPE,
        ARGUMENT_TABLE_EXISTED,
        TAG
    }
}
