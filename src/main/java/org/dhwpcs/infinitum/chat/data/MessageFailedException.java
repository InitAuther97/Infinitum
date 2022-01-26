package org.dhwpcs.infinitum.chat.data;

public class MessageFailedException extends Exception {

    private Type exType = null;

    public MessageFailedException(Throwable cause) {
        super(cause);
        if(cause instanceof BrokenMsgException) {
            exType = Type.BROKEN_MESSAGE;
        }
    }

    public MessageFailedException(Throwable cause, Type t) {
        super(cause);
        exType = t;
    }

    public Type getType() {
        return exType;
    }

    public MessageFailedException(int msg) {
        super(String.format("Msg %d is not found", msg));
    }

    public MessageFailedException(Type type) {
        super("INTERNAL CAPTURE:"+type);
    }

    public enum Type {
        CHUNK_NOT_FOUND,
        MSG_PARSE_FAILED,
        NO_SUCH_MESSAGE,
        BROKEN_MESSAGE
    }
}
