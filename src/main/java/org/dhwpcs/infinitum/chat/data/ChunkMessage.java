package org.dhwpcs.infinitum.chat.data;

import java.nio.ByteBuffer;
import java.util.ArrayList;

public record ChunkMessage(int begin, int length, ArrayList<Message> messages) {
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
}
