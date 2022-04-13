package org.dhwpcs.infinitum.chat.tag.parse;

import com.google.common.base.Preconditions;
import io.github.initauther97.nugget.util.RangeTree;
import org.dhwpcs.infinitum.chat.adventure.Translatable;
import org.dhwpcs.infinitum.chat.tag.Parseable;
import org.dhwpcs.infinitum.chat.tag.TagInstance;
import org.dhwpcs.infinitum.chat.tag.parse.op.Opcode;
import org.dhwpcs.infinitum.chat.tag.parse.op.Type;

import java.io.Closeable;
import java.util.*;

public class TagDataWriter implements Closeable, RangeTree.Range {
    public TagDataWriter(int begin) {
        Preconditions.checkArgument(begin >= 0);
        this.begin = begin;
    }
    private final int begin;
    private int end = -1;
    private String id;
    private Map<String, Object> properties = new HashMap<>();
    private Set<Object> attributes = new HashSet<>();
    private Stack<Object> stack = new Stack<>();
    private List<Parseable> content = new LinkedList<>();
    private Type type = Type.ID;
    private boolean pair = true;
    private boolean terminated = false;

    /**
     * Set the tag unpaired.
     * This is a terminate operation.
     */
    public void unpaired() {
        Preconditions.checkState(!terminated);
        pair = false;
        close();
    }

    public boolean isPair() {
        return pair;
    }

    /**
     * Define a property for the specified tag instance
     * @param key key
     * @param value value
     */
    public void writeProperty(String key, Object value) {
        Preconditions.checkState(!terminated);
        properties.put(key, value);
    }

    void writePropertyReversed(Object value, String key) {
        writeProperty(key, value);
    }

    /**
     * Define a attribute for the specified tag instance
     * @param attr Attribute
     */
    public void writeAttribute(String attr) {
        Preconditions.checkState(!terminated);
        attributes.add(attr);
    }

    /**
     * Specify the id of the tag.
     * @param id
     */
    public void writeId(String id) {
        Preconditions.checkState(!terminated);
        this.id = id;
    }

    //Opcode based operations

    /**
     * Push a Object into the var stack.
     * @param obj to push
     */
    public void push(Object obj) {
        stack.push(obj);
    }

    /**
     * Eval the specific operation code.
     * @param op to eval
     */
    public void execute(Opcode op) {
        switch (op) {
            case ID -> writeId((String) stack.pop());
            case POP -> stack.pop();
            case PROPERTY -> {
                if(stack.size() == 1) {
                    writeAttribute((String) stack.pop());
                } else writePropertyReversed(stack.pop(), (String) stack.pop());
            }
            case ATTRIBUTE -> writeAttribute((String) stack.pop());
            case TERMINATE -> close();
        }
    }

    /**
     * Eval the opcodes with the var stack provided.
     * This is a terminate operation.
     * @param opcodes Operations
     * @param variables Variable stack
     */
    public void eval(Deque<Opcode> opcodes, Deque<Object> variables) {
        while(!opcodes.isEmpty()) {
            switch (opcodes.pop()) {
                case ID -> writeId((String) variables.pop());
                case POP -> variables.pop();
                case PROPERTY -> writeProperty((String) variables.pop(), variables.pop());
                case ATTRIBUTE -> writeAttribute((String) variables.pop());
            }
        }
        close();
    }

    //Streaming write operation
    public void write(Object o) {
        Preconditions.checkArgument(!(o instanceof Opcode), "Opcodes should be used in streaming operations");
        switch (type) {
            case ID -> {
                writeId(o.toString());
                type = Type.PROPERTY;
            }
            case PROPERTY -> {
                stack.push(o.toString());
                if(stack.size() == 2) {
                    writePropertyReversed(o, stack.pop().toString());
                }
            }
            case ATTRIBUTE -> writeAttribute(o.toString());
            case CONTENT -> {
                if(o instanceof TagInstance t) {
                    if(t == TagInstance.END_TAG) {
                        close();
                    } else {
                        content.add(t);
                    }
                } else {
                    content.add(Parseable.plain(o.toString()));
                }
            }
        }
    }

    public void write(Type t) {
        this.type = t;
    }


    public Type getType() {
        return type;
    }

    //After termination operations

    public String getId() {
        Preconditions.checkState(terminated);
        return id;
    }

    public Map<String, Object> getProperties() {
        Preconditions.checkState(terminated);
        return properties;
    }

    public Set<Object> getAttributes() {
        Preconditions.checkState(terminated);
        return attributes;
    }

    @Override
    public void close() {
        terminated = true;
    }

    public void reset() {
        id = null;
        properties.clear();
        attributes.clear();
        terminated = false;
    }

    @Override
    public int begin() {
        return begin;
    }

    @Override
    public int end() {
        return end;
    }

    public void setEnd(int i) {
        Preconditions.checkArgument(!terminated);
        this.end = i;
    }

    public boolean isTerminated() {
        return terminated;
    }

    public Parseable getContent() {
        return Parseable.compose(content.toArray(Parseable[]::new));
    }
}
