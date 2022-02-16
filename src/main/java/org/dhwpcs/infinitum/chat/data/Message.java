package org.dhwpcs.infinitum.chat.data;

import com.google.common.base.Preconditions;
import io.github.initauther97.silverfrost.je.network.BasicCodec;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;

import java.io.UTFDataFormatException;
import java.nio.ByteBuffer;
import java.time.Instant;
import java.time.temporal.Temporal;
import java.util.Date;
import java.util.UUID;

public record Message(int id, long timestamp, int tick, UUID sender, Component message) {

    public Date timeAsDate() {
        return new Date(timestamp);
    }

    public Temporal timeAsTemporal() {
        return Instant.ofEpochMilli(timestamp);
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
        BasicCodec.writeUUID(msg.sender, buffer);
        BasicCodec.writeString(GsonComponentSerializer.gson().serialize(msg.message), buffer);
        buffer.flip();
        return buffer;
    }

    public static final class Builder {
        private long timestamp = -1;
        private int tick = -1;
        private UUID sender = null;
        private Component message = null;

        public Builder timestamp(long t) {
            timestamp = t;
            return this;
        }

        public Builder tick(int t) {
            tick = t;
            return this;
        }

        public Builder uid(UUID uuid) {
            sender = uuid;
            return this;
        }

        public Builder message(Component message) {
            this.message = message;
            return this;
        }

        public Message build(int id) {
            Preconditions.checkNotNull(sender);
            Preconditions.checkNotNull(message);
            Preconditions.checkArgument(timestamp > 0);
            Preconditions.checkArgument(id > 0);
            return new Message(id, timestamp, tick, sender, message);
        }
    }
}
