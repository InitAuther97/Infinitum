package org.dhwpcs.infinitum.chat.brigadier.exception;

import com.mojang.brigadier.Message;

public class ArgumentExceptionMessage implements Message {

    public static final ArgumentExceptionMessage MULTIPLE_TARGET = new ArgumentExceptionMessage(Predefined.MULTIPLE_TARGET);

    private final String msg;

    @Override
    public String getString() {
        return msg;
    }

    public enum Predefined {
        MULTIPLE_TARGET("Requested just one target, but found multi");
        private final String message;

        Predefined(String message) {
            this.message = message;
        }
    }

    public ArgumentExceptionMessage(Predefined type) {
        this.msg = type.message;
    }
}
