package io.github.initauther97.arguments;

public interface ParseErrorHandler {
    ParseErrorHandler DEFAULT = (index, arg, val, e) -> ErrorOperation.TERMINATE;

    ErrorOperation parseFailed(int index, IArgument<?> argument, String value, Exception e);
}
