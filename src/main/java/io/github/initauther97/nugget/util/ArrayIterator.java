package io.github.initauther97.nugget.util;

import java.util.Iterator;

public class ArrayIterator<T> implements Iterator<T> {

    private int pointer;
    private final int limit;
    private final T[] elements;

    public ArrayIterator(T[] elements) {
        limit = elements.length;
        pointer = 0;
        this.elements = elements;
    }

    public ArrayIterator(T[] elements, int begin, int limit) {
        this.limit = limit;
        pointer = begin;
        this.elements = elements;
    }
    @Override
    public boolean hasNext() {
        return pointer < limit;
    }

    @Override
    public T next() {
        return elements[pointer++];
    }
}
