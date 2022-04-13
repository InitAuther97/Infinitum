package io.github.initauther97.nugget.util.stream;

import io.github.initauther97.nugget.util.ArrayIterator;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public abstract class SimpleUnsafeStream<T> implements Stream<T> {

    private static byte ORDERED =  0b00000001;
    private static byte DISTINCT = 0b00000010;
    private static byte NONNULL =  0b00000100;
    private static byte PARALLEL = 0b00001000;

    private static final float RESIZE_FACTOR = 1.7f;
    private static final int INITIAL_SIZE = 10;
    /*private static final Object NULL = new Object(){
        @Override
        public int hashCode() {
            return 0;
        }

        @Override
        public String toString() {
            return "<Representation of NULL>";
        }
    };*/

    private Object[] storage;
    private int limit;
    private boolean terminate = false;
    private byte characteristics = 0b00000000;
    private RuntimeException exception;

    public SimpleUnsafeStream(Collection<? extends T> collection) {
        if(collection.isEmpty()) {
            throw new IllegalArgumentException();
        }
        storage = collection.toArray();
        limit = storage.length;
    }

    public SimpleUnsafeStream(Iterable<? extends T> iterable) {
        limit = 0;
        storage = new Object[INITIAL_SIZE];
        Iterator<? extends T> iterator = iterable.iterator();
        while(iterator.hasNext()) {
            append(iterator.next());
        }
    }

    public SimpleUnsafeStream(T[] array) {
        this.storage = array.clone();
        this.limit = array.length;
    }


    private void sortUp() {
        //The marker representing a null value
        int j = 0;
        for(int i = 0;i < limit;i++) {
            for(;storage[j] != null && j < limit;j++) {}
            if(j == limit - 1 && storage[j] != null) {
                break;
            }
            if(storage[i] != null){
                storage[j] = storage[i];
                storage[i] = null;
            }
        }
        limit = j;
    }

    private void trim() {
        Object[] cache = storage;
        storage = new Object[limit];
        System.arraycopy(cache, 0, storage, 0, limit);
    }

    private void append(Object obj) {
        set(limit++, obj);
        if(limit == storage.length) {
            expand();
        }
    }

    private void expand() {
        int newLen = (int) (storage.length * RESIZE_FACTOR);
        Object[] cache = storage;
        storage = new Object[newLen];
        System.arraycopy(cache, 0, storage, 0, cache.length);
    }

    private void appendAll(Object[] obj) {
        int progress = 0;
        int len;
        int k = limit;
        while(progress < obj.length) {
            len = Math.min(obj.length - 1 - progress, storage.length - limit);
            System.arraycopy(obj, progress, storage, limit, len);
            limit += len;
            progress = len;
        }
    }

    private boolean failed() {
        return exception != null;
    }

    /**
     * Check whether the stream has one of the characteristics.
     * @param characteristic Characteristics. Connected with &
     * @return whether with one of them.
     */
    private boolean hasChar(byte characteristic) {
        return (characteristics & characteristic) != 0;
    }

    private void addChar(byte characteristic) {
        characteristics |= characteristic;
    }

    private void removeChar(byte characteristic) {
        characteristics &= ~characteristic;
    }

    private T get(int ptr) {
        return (T) storage[ptr];
    }

    private void set(int ptr, Object t) {
        storage[ptr] = t;
    }

    @Override
    public Stream<T> filter(Predicate<? super T> predicate) {
        checkState();
        if(!failed()) {
            int pointer = 0;
            while (pointer < limit) {
                if (!predicate.test(get(pointer))) {
                    set(pointer, null);
                }
                pointer++;
            }
            sortUp();
        }
        return this;
    }

    @Override
    public <R> Stream<R> map(Function<? super T, ? extends R> mapper) {
        checkState();
        if(!failed()) {
            int pointer = 0;
            while (pointer < limit) {
                set(pointer, mapper.apply(get(pointer)));
                pointer++;
            }
        }
        return (Stream<R>) this;
    }

    @Override
    public IntStream mapToInt(ToIntFunction<? super T> mapper) {
        throw new UnsupportedOperationException();
    }

    @Override
    public LongStream mapToLong(ToLongFunction<? super T> mapper) {
        throw new UnsupportedOperationException();
    }

    @Override
    public DoubleStream mapToDouble(ToDoubleFunction<? super T> mapper) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <R> Stream<R> flatMap(Function<? super T, ? extends Stream<? extends R>> mapper) {
        checkState();
        if(!failed()) {
            int pointer = 0;
            Stream<? extends R>[] streams = new Stream[limit];
            while (pointer < limit) {
                streams[pointer] = mapper.apply(get(pointer));
                pointer++;
            }
            int size = 0;
            for(Stream<? extends R> stream : streams) {
                size += stream.count();
            }
            storage = new Object[size];
            limit = 0;
            for(Stream<? extends R> stream : streams) {
                appendAll(stream.toArray());
            }
        }
        return (Stream<R>) this;
    }

    @Override
    public IntStream flatMapToInt(Function<? super T, ? extends IntStream> mapper) {
        throw new UnsupportedOperationException();
    }

    @Override
    public LongStream flatMapToLong(Function<? super T, ? extends LongStream> mapper) {
        throw new UnsupportedOperationException();
    }

    @Override
    public DoubleStream flatMapToDouble(Function<? super T, ? extends DoubleStream> mapper) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Stream<T> distinct() {
        checkState();
        if (!failed()){
            for (int i = 0; i < limit; i++) {
                for (int j = i + 1; j < limit; j++) {
                    if (storage[i] != null && storage[i].equals(storage[j])) {
                        storage[j] = null;
                    }
                }
            }
            sortUp();
        }
        return this;
    }

    @Override
    public Stream<T> sorted() {
        checkState();
        if(!failed() && !hasChar(ORDERED)) {
            try {
                Arrays.sort(storage, 0, limit);
            } catch (ClassCastException e) {
                if (exception == null) {
                    exception = e;
                } else exception.addSuppressed(e);
                return this;
            }
            addChar(ORDERED);
        }
        return this;
    }

    @Override
    public Stream<T> sorted(Comparator<? super T> comparator) {
        checkState();
        if(!failed() && !hasChar(ORDERED)) {
            try {
                Arrays.sort(storage, 0, limit, (f,s) -> comparator.compare((T)f, (T)s));
            } catch (ClassCastException e) {
                if (exception == null) {
                    exception = e;
                } else exception.addSuppressed(e);
            }
            addChar(ORDERED);
        }
        return this;
    }

    @Override
    public Stream<T> peek(Consumer<? super T> action) {
        checkState();
        if(!failed()) {
            for (int i = 0; i < limit; i++) {
                action.accept(get(i));
            }
        }
        return this;
    }

    @Override
    public Stream<T> limit(long maxSize) {
        checkState();
        if(!failed() && maxSize < limit) {
            limit = (int) maxSize;
        }
        return this;
    }

    @Override
    public Stream<T> skip(long n) {
        checkState();
        if (!failed()){
            for (int i = (int) n; i < limit; i++) {
                set((int) (i-n),get(i));
            }
            limit -= n;
        }
        return this;
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        checkState();
        if(failed()) {
            throw exception;
        }
        for(int i = 0; i < limit; i++) {
            action.accept(get(i));
        }
        close();
    }

    @Override
    public void forEachOrdered(Consumer<? super T> action) {
        checkState();
        if(failed()) {
            throw exception;
        }
        for(int i = 0; i < limit; i++) {
            action.accept(get(i));
        }
        close();
    }

    @NotNull
    @Override
    public Object[] toArray() {
        checkState();
        if(failed()) {
            throw exception;
        }
        trim();
        close();
        return storage.clone();
    }

    @NotNull
    @Override
    public <A> A[] toArray(IntFunction<A[]> generator) {
        checkState();
        if(failed()) {
            throw exception;
        }
        A[] result = generator.apply(limit);
        for(int i = 0; i < limit; i++) {
            result[i] = (A) get(i);
        }
        close();
        return result;
    }

    @Override
    public T reduce(T identity, BinaryOperator<T> accumulator) {
        checkState();
        if(failed()) {
            throw exception;
        }
        if(limit > 0) {
            T result = get(0);
            for (int i = 1; i < limit; i++) {
                result = accumulator.apply(result, get(i));
            }
            close();
            return result;
        } else {
            close();
            return identity;
        }
    }

    @NotNull
    @Override
    public Optional<T> reduce(BinaryOperator<T> accumulator) {
        checkState();
        if(failed()) {
            throw exception;
        }
        if(limit > 0) {
            T result = get(0);
            for (int i = 1; i < limit; i++) {
                result = accumulator.apply(result, get(i));
            }
            close();
            return Optional.of(result);
        } else {
            close();
            return Optional.empty();
        }
    }

    @Override
    public <U> U reduce(U identity, BiFunction<U, ? super T, U> accumulator, BinaryOperator<U> combiner) {
        checkState();
        if(failed()) {
            throw exception;
        }
        if(limit > 0) {
            U result = identity;
            for (int i = 1; i < limit; i++) {
                result = accumulator.apply(result, get(i));
            }
            close();
            return result;
        } else {
            close();
            return identity;
        }
    }

    @Override
    public <R> R collect(Supplier<R> supplier, BiConsumer<R, ? super T> accumulator, BiConsumer<R, R> combiner) {
        checkState();
        if(failed()) {
            throw exception;
        }
        R result = supplier.get();
        if(limit > 0) {
            for (int i = 1; i < limit; i++) {
                accumulator.accept(result, get(i));
            }
        }
        close();
        return result;
    }

    @Override
    public <R, A> R collect(Collector<? super T, A, R> collector) {
        checkState();
        if(failed()) {
            throw exception;
        }
        A result = collector.supplier().get();
        if(limit > 0) {
            for (int i = 1; i < limit; i++) {
                collector.accumulator().accept(result, get(i));
            }
        }
        close();
        return collector.finisher().apply(result);
    }

    @NotNull
    @Override
    public Optional<T> min(Comparator<? super T> comparator) {
        checkState();
        if(failed()) {
            throw exception;
        }
        if(limit == 0)return Optional.empty();
        sorted(comparator);
        close();
        return Optional.of(get(0));
    }

    @NotNull
    @Override
    public Optional<T> max(Comparator<? super T> comparator) {
        checkState();
        if(failed()) {
            throw exception;
        }
        if(limit == 0)return Optional.empty();
        sorted(comparator);
        close();
        return Optional.of(get(limit-1));
    }

    @Override
    public long count() {
        return limit;
    }

    @Override
    public boolean anyMatch(Predicate<? super T> predicate) {
        checkState();
        if(failed()) {
            throw exception;
        }
        if(limit == 0)return false;
        close();
        for (int i = 0; i < limit; i++) {
            if(predicate.test(get(i)))return true;
        }
        return false;
    }

    @Override
    public boolean allMatch(Predicate<? super T> predicate) {
        checkState();
        if(failed()) {
            throw exception;
        }
        if(limit == 0)return true;
        close();
        for (int i = 0; i < limit; i++) {
            if(!predicate.test(get(i)))return false;
        }
        return true;
    }

    @Override
    public boolean noneMatch(Predicate<? super T> predicate) {
        checkState();
        if(failed()) {
            throw exception;
        }
        if(limit == 0)return true;
        close();
        for (int i = 0; i < limit; i++) {
            if(predicate.test(get(i)))return false;
        }
        return true;
    }

    @NotNull
    @Override
    public Optional<T> findFirst() {
        checkState();
        if(failed()) {
            throw exception;
        }
        if(limit == 0)return Optional.empty();
        close();
        return Optional.of(get(0));
    }

    @NotNull
    @Override
    public Optional<T> findAny() {
        checkState();
        if(failed()) {
            throw exception;
        }
        return findFirst();
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        checkState();
        if(failed()) {
            throw exception;
        }
        return (Iterator<T>) new ArrayIterator<>(storage, 0, limit);
    }

    @NotNull
    @Override
    public Spliterator<T> spliterator() {
        checkState();
        if(failed()) {
            throw exception;
        }
        return Spliterators.spliterator(storage, 0, limit, toSpliteratorCharacteristics());
    }

    @Override
    public boolean isParallel() {
        checkState();
        return false;
    }

    @NotNull
    @Override
    public Stream<T> sequential() {
        checkState();
        return this;
    }

    @NotNull
    @Override
    public Stream<T> parallel() {
        checkState();
        throw new UnsupportedOperationException();
    }

    @NotNull
    @Override
    public Stream<T> unordered() {
        checkState();
        return this;
    }

    @NotNull
    @Override
    public Stream<T> onClose(Runnable closeHandler) {
        checkState();
        closeHandlers.add(closeHandler);
        return this;
    }

    @Override
    public void close() {
        checkState();
        terminate = true;
        closeHandlers.forEach(Runnable::run);
    }

    private void checkState() {
        if(terminate) {
            throw new IllegalStateException();
        }
    }

    private Set<Runnable> closeHandlers = new HashSet<>();

    private int toSpliteratorCharacteristics() {
        int result = 0;
        if(hasChar(DISTINCT))result |= Spliterator.DISTINCT;
        if(hasChar(NONNULL))result |= Spliterator.NONNULL;
        if(hasChar(PARALLEL))result |= Spliterator.CONCURRENT;
        if(hasChar(ORDERED))result |= Spliterator.ORDERED;
        return result;
    }
}
