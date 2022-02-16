package org.dhwpcs.infinitum.chat.data;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ChunkMessage {
    final int begin;
    int length;
    final List<Message> messages;
    final boolean appendable;

    public ChunkMessage(int begin, int length, List<Message> messages) {
        this.begin = begin;
        this.length = length;
        this.messages = messages;
        this.appendable = false;
    }

    public ChunkMessage(int begin) {
        this.begin = begin;
        this.length = 0;
        this.messages = new LinkedList<>();
        this.appendable = true;
    }

    public ChunkMessage(int begin, List<Message> messages) {
        this.begin = begin;
        this.length = messages.size();
        this.messages = messages;
        this.appendable = true;
    }

    public Message get(int id) {
        return messages.get(id - begin);
    }

    public static ChunkMessage decode(ByteBuffer buffer) throws MessageFailedException {
        int begin = buffer.getInt();
        int length = buffer.getInt();
        ArrayList<Message> messages = new ArrayList<>(length);
        while(length-- > 0) {
            messages.add(Message.decode(buffer));
        }
        return new ChunkMessage(begin, length, messages);
    }

    public static void encode(ByteBuffer buffer, ChunkMessage message) {
        buffer.putInt(message.begin);
        buffer.putInt(message.length);
        for(Message msg : message.messages) {
            buffer.put(Message.encode(msg));
        }
    }

    void append(Message msg) {
        messages.add(msg);
        length++;
    }
}
