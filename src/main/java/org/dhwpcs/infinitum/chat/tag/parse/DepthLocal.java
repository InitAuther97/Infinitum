package org.dhwpcs.infinitum.chat.tag.parse;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Supplier;

public class DepthLocal<T> {
    private final List<T> depth = new LinkedList<>();
    private final ListIterator<T> iter = depth.listIterator();
    private final Supplier<T> constructor;

    public DepthLocal(Supplier<T> supplier) {
        constructor = supplier;
    }

    public T deeper() {
        if(!iter.hasPrevious()) {
            iter.add(constructor.get());
        }
        return iter.previous();
    }

    public T shallower() {
        return iter.next();
    }

    public boolean hasShallower() {
        return iter.nextIndex() != -1;
    }

    public void drop() {
        while(iter.hasPrevious())iter.previous();
        while(iter.hasNext()) {
            iter.next();
            iter.remove();
        }
    }
}
