package org.dhwpcs.infinitum.chat.data;

import java.util.Objects;

public record MsgFileInfo(int begin, int length, String file, boolean isFinal) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MsgFileInfo)) return false;
        MsgFileInfo that = (MsgFileInfo) o;
        return begin == that.begin && length == that.length && isFinal == that.isFinal;
    }

    @Override
    public int hashCode() {
        return Objects.hash(begin, length, isFinal);
    }
}
