package org.dhwpcs.infinitum.chat.tag.parse;

import io.github.initauther97.nugget.util.RangeTree;

import java.util.function.BiFunction;

public class StringRange implements RangeTree.Range, Cloneable {
    private int begin;
    private int end;

    public StringRange() {
        this.begin = this.end = 0;
    }

    public StringRange(int begin) {
        this.end = this.begin = 0;
    }

    public StringRange(int begin, int end) {
        this.begin = begin;
        this.end = end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public void setBegin(int begin) {
        this.begin = begin;
        this.end = end < begin ? begin : end;
    }

    @Override
    public int begin() {
        return begin;
    }

    @Override
    public int end() {
        return end;
    }

    public<R> R transform(BiFunction<Integer, Integer, R> function) {
        return function.apply(begin, end);
    }

    @Override
    public StringRange clone() {
        try {
            StringRange clone = (StringRange) super.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
