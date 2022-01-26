package io.github.initauther97.arguments;

import io.github.initauther97.arguments.env.Environment;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.LinkedList;

public class ArgumentList {

    private static final Object[] EMPTY_RESULT = new Object[0];
    private final ParseErrorHandler handler;
    /**
     * For efficiency
     */
    public static final ArgumentList EMPTY = new ArgumentList() {
        @Override
        public boolean add(IArgument<?> argument) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int getRequiredCount() {
            return 0;
        }

        @Override
        public int getOptionalCount() {
            return 0;
        }

        @Nullable
        @Override
        public Object[] fullParse(String[] args, int offset) {
            if(args.length - offset != 0) {
                return null;
            }
            return EMPTY_RESULT;
        }

        @Override
        public Object[] partParse(String[] args, int offset) {
            if(args.length - offset != 0) {
                return null;
            }
            return EMPTY_RESULT;
        }
    };

    private final Environment env;
    private final LinkedList<IArgument<?>> required = new LinkedList<>();
    private final LinkedList<IArgument<?>> optional = new LinkedList<>();

    public boolean add(IArgument<?> argument) {
        if (argument.getDefaultValue() != null) {
            return optional.add(argument);
        } else {
            return required.add(argument);
        }
    }

    public ArgumentList(IArgument<?>... arguments) {
        this(Environment.DEFAULT, arguments);
    }

    public ArgumentList(ParseErrorHandler handler, IArgument<?>... arguments) {
        this(Environment.DEFAULT, handler, arguments);
    }

    public ArgumentList(Environment env, IArgument<?>... arguments) {
        this(env, ParseErrorHandler.DEFAULT, arguments);
    }

    public ArgumentList(Environment env, ParseErrorHandler handler, IArgument<?>... arguments) {
        this.env = env;
        for (IArgument<?> argument : arguments) {
            if(argument.checkCompatible(env)) {
                add(argument);
            } else throw new UnsupportedArgumentException("Compatibility check failed.");
        }
        this.handler = handler;
    }


    public int getRequiredCount() {
        return required.size();
    }

    public int getOptionalCount() {
        return optional.size();
    }

    @Nullable
    public Object[] fullParse(String[] args, int offset) {
        int length = args.length - offset;
        if(length >= getRequiredCount() && length <= getRequiredCount() + getOptionalCount()) {
            Object[] result = new Object[getRequiredCount() + getOptionalCount()];
            Iterator<IArgument<?>> iter = required.iterator();
            int base = offset;
            int ptr = 0;
            for (; base < args.length; base++, ptr++) {
                IArgument<?> arg = iter.next();
                try {
                    result[ptr] = arg.parse(args[base], env);
                } catch (Exception e) {
                    switch (handler.parseFailed(base, arg, args[base], e)) {
                        case TERMINATE -> throw (e instanceof RuntimeException ? (RuntimeException) e : new RuntimeException(e));
                    }
                }
            }
            while (iter.hasNext()) {
                result[ptr++] = iter.next().getDefaultValue();
            }
            return result;
        }
        return null;
    }

    public Object[] partParse(String[] args, int offset) {
        int length = args.length - offset;
        if(length <= getRequiredCount() + getOptionalCount()) {
            Object[] result = new Object[length];
            Iterator<IArgument<?>> iter = required.iterator();
            for (int base = offset, ptr = 0; base < args.length; base++, ptr++) {
                IArgument<?> arg = iter.next();
                try {
                    result[ptr] = arg.parse(args[base], env);
                } catch (Exception e) {
                    result[ptr] = null;
                }
            }
            return result;
        }
        return null;
    }
}
