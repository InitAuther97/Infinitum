package io.github.initauther97.nugget.util;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.ThreadSafe;
import java.util.*;
import java.util.function.Function;

@ThreadSafe
public class RangeTree<T extends RangeTree.Range> implements Iterable<RangeTree.Node<T>> {
    private final Node<T> root;
    public RangeTree(int limit) {
        Preconditions.checkArgument(limit > 0);
        root = createRoot(limit);
    }

    RangeTree(Node<T> root) {
        this.root = root;
    }

    public<E extends Range> RangeTree<E> map(Function<T,E> mapper) {
        return new RangeTree<>(root.map(mapper));
    }

    private static<E extends Range> Node<E> createRoot(int max) {
        return new Node<>(null) {
            @Override
            public int begin() {
                return 0;
            }

            @Override
            public int end() {
                return max;
            }

            @Override
            public boolean contains(Node<E> another) {
                return another.begin() >= 0 && another.end() <= max;
            }

            @Override
            public boolean contained(E another) {
                return false;
            }

            @Override
            public boolean contains(E another) {
                return another.begin() >= 0 && another.end() <= max;
            }
        };
    }

    public static<E extends Range> RangeTree<E> create(int max, Collection<E> coll) {
        for(E e1 : coll) {
            for(E e2 : coll) {
                if(e1.intersectsNoContain(e2)) {
                    throw new IllegalArgumentException("Intersection");
                }
            }
        }

        if(coll.size() == 1) {
            Node<E> root = createRoot(max);
            root.append(new Node<>(coll.iterator().next()));
            return new RangeTree<>(root);
        } else if(coll.size() == 2) {
            Node<E> root = createRoot(max);
            Iterator<E> iter = coll.iterator();
            Node<E> first = new Node<>(iter.next());
            Node<E> second = new Node<>(iter.next());
            if(second.contains(first)) {
                second.append(first);
                root.append(second);
            } else if(first.contains(second)) {
                first.append(second);
                root.append(first);
            } else {
                root.append(first);
                root.append(second);
            }
            return new RangeTree<>(root);
        }

        List<E> list = new ArrayList<>(coll);
        list.sort(Comparator.comparing(Range::begin));
        RangeTree<E> tree = new RangeTree<>(max);
        Deque<Node<E>> deque = new LinkedList<>();
        deque.push(tree.root);
        Node<E> parent;
        Iterator<E> iter = list.iterator();

        E now;
        while(iter.hasNext()) {
            now = iter.next();
            while (!deque.isEmpty()) {
                parent = deque.pop();
                if (parent.contains(now)) {
                    Node<E> nowNode = new Node<>(now);
                    parent.append(nowNode);
                    deque.push(parent);
                    deque.push(nowNode);
                    break;
                }
            }
        }

        return tree;
    }

    @NotNull
    @Override
    public Iterator<Node<T>> iterator() {
        return new RTIterator<>(this);
    }

    public static class RTIterator<T extends Range> implements Iterator<Node<T>> {

        private final RangeTree<T> tree;
        private final Deque<ListIterator<Node<T>>> iteratorStack = new LinkedList<>();
        private ListIterator<Node<T>> now;
        private boolean reach = false;

        public RTIterator(RangeTree<T> rangeTree) {
            tree = rangeTree;
            iteratorStack.push(tree.root.children.listIterator());
            tryReach();
        }

        @Override
        public boolean hasNext() {
            return reach;
        }

        @Override
        public Node<T> next() {
            if(!reach) {
                if(!tryReach()){
                    throw new NoSuchElementException();
                }
            }
            reach = false;
            return now.next();
        }

        private boolean tryReach() {
            ListIterator<Node<T>> now;
            Node<T> nowNode;
            while (!iteratorStack.isEmpty()) {
                now = iteratorStack.pop();
                while (now.hasNext()) {
                    nowNode = now.next();
                    if (nowNode.hasChild()) {
                        iteratorStack.push(now);
                        iteratorStack.push(nowNode.childIterator());
                    } else {
                        now.previous();
                        this.now = now;
                        reach = true;
                        return true;
                    }
                }
            }
            reach = false;
            return false;
        }
    }

    public static class Node<T extends Range> {
        private final List<Node<T>> children = new LinkedList<>();
        private T instance;
        private Node<T> parent;

        public Node(T instance) {
            this.instance = instance;
        }

        public void append(Node<T> node) {
            children.add(node);
            node.parent(this);
        }

        public boolean contains(Node<T> another) {
            return instance.contains(another.instance);
        }

        public boolean contained(T another) {
            return another.contains(instance);
        }

        public boolean contains(T another) {
            return instance.contains(another);
        }

        public int begin() {
            return instance.begin();
        }

        public int end() {
            return instance.end();
        }

        public boolean hasChild() {
            return !children.isEmpty();
        }

        public ListIterator<Node<T>> childIterator() {
            return children.listIterator();
        }

        public <E extends Range> Node<E> map(Function<T, E> mapper) {
            Node<E> res = new Node<>(mapper.apply(instance));
            children.forEach(child -> res.append(child.map(mapper)));
            return res;
        }

        public void transform(Function<T,T> transformer) {
            instance = transformer.apply(instance);
        }

        public void parent(Node<T> parent) {
            this.parent = parent;
        }
    }

    /**
     * Left:Inclusive
     * Right:Exclusive
     */
    public interface Range {
        int begin();
        int end();

        default boolean isPoint() {
            return begin() == end();
        }

        default int relativize(Range another) {
            if(getClass().isInstance(another) && contains(another)) {
                return this.begin() - another.begin();
            }
            return Integer.MIN_VALUE;
        }

        default boolean contains(Range r) {
            return r.begin() > begin() && r.end() < end();
        }

        default boolean intersects(Range r) {
            return contains(r) || r.contains(this) || intersectsNoContain(r);
        }

        default boolean intersectsNoContain(Range r) {
            return r.begin() > begin() && r.begin() < end() && r.end() < begin()
                    || r.end() < end() && r.end() < begin() && r.begin() > begin();
        }
    }
}
