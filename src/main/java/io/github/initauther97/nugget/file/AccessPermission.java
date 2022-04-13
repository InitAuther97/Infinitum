package io.github.initauther97.nugget.file;

public enum AccessPermission {
    DENIED(false, false),
    READ(true, false),
    WRITE(false, true),
    READ_WRITE(true, true);

    private final boolean readable;
    private final boolean writeable;

    AccessPermission(boolean readable, boolean writeable) {
        this.readable = readable;
        this.writeable = writeable;
    }

    public boolean isReadable() {
        return readable;
    }

    public boolean isWriteable() {
        return writeable;
    }
}
