package org.dhwpcs.infinitum.chat.data;

public class BrokenMsgException extends Exception {
    public BrokenMsgException() {
    }

    public BrokenMsgException(String message) {
        super(message);
    }

    public BrokenMsgException(String message, Throwable cause) {
        super(message, cause);
    }

    public BrokenMsgException(Throwable cause) {
        super(cause);
    }
}
