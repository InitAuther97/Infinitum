package org.dhwpcs.infinitum.chat.data;

import io.github.initauther97.silverfrost.je.network.BasicCodec;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;

import java.io.UTFDataFormatException;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.UUID;

public record Message(int id, long timestamp, int tick, UUID sender, Component message) {
    public Date timeAsDate() {
        return new Date(timestamp);
    }
    public static Message decode(ByteBuffer buffer) throws MessageFailedException {
        int id = buffer.getInt();
        long timestamp = buffer.getLong();
        int tick = buffer.getInt();
        UUID sender = BasicCodec.readUUID(buffer);
        Component component;
        try {
            component = GsonComponentSerializer.gson().deserialize(BasicCodec.readString(buffer));
        } catch (UTFDataFormatException e) {
            throw new MessageFailedException(e, MessageFailedException.Type.BROKEN_MESSAGE);
        }
        return new Message(id, timestamp, tick, sender, component);
    }

    public static ByteBuffer encode(Message msg) {
        ByteBuffer buffer = ByteBuffer.allocate(4+8+4+16+32767);
        buffer.putInt(msg.id);
        buffer.putLong(msg.timestamp);
        buffer.putInt(msg.tick);
        buffer.putLong(msg.sender.getMostSignificantBits());
        buffer.putLong(msg.sender.getLeastSignificantBits());
        BasicCodec.writeString(GsonComponentSerializer.gson().serialize(msg.message), buffer);
        buffer.flip();
        return buffer;
    }
}
