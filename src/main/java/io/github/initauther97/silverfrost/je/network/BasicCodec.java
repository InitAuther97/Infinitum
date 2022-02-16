package io.github.initauther97.silverfrost.je.network;

import java.io.UTFDataFormatException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class BasicCodec {
    public static void writeVarInt(int value, ByteBuffer buf) {
        do {
            byte temp = (byte) (value & 0b01111111);
            value >>>= 7;
            if (value != 0) {
                temp |= 0b10000000;
            }
            buf.put(temp);
        } while (value != 0);
    }

    public static int readVarInt(ByteBuffer buf) {
        int numRead = 0;
        int result = 0;
        byte read;
        do {
            read = buf.get();
            int value = (read & 0b01111111);
            result |= (value << (7 * numRead));
            numRead++;
            if (numRead > 5) {
                throw new RuntimeException("VarInt is too long");
            }
        } while ((read & 0b10000000) != 0);
        return result;
    }

    public static void writeVarLong(long value, ByteBuffer buf) {
        do {
            byte temp = (byte) (value & 0b01111111);
            value >>>= 7;
            if (value != 0) {
                temp |= 0b10000000;
            }
            buf.put(temp);
        } while (value != 0);
    }

    public static long readVarLong(ByteBuffer buf) {
        int numRead = 0;
        long result = 0;
        byte read;
        do {
            read = buf.get();
            long value = (read & 0b01111111);
            result |= (value << (7 * numRead));

            numRead++;
            if (numRead > 10) {
                throw new RuntimeException("VarLong is too big");
            }
        } while ((read & 0b10000000) != 0);

        return result;
    }

    public static void writeString(String str, ByteBuffer buf) {
        if(str.length()>32767){
            throw new IllegalArgumentException("String is too long");
        }
        writeVarInt(str.length(),buf);
        buf.put(str.getBytes(StandardCharsets.UTF_8));
    }

    public static String readString(ByteBuffer buf) throws UTFDataFormatException {
        int length = readVarInt(buf);
        if(length > 32767){
            throw new IllegalArgumentException("String to receive is too long");
        }
        byte[] data = new byte[64];
        int pointer = 0;
        byte cache;
        byte bteLeft = 0;
        while(length > 0){
            cache = buf.get();
            if(bteLeft-- > 0) {
                if((cache & 0xC0) == 0x80){
                    throw new UTFDataFormatException();
                }
                data[pointer++] = cache;
                continue;
            } else {
                bteLeft = 0;
                length--;
            }
            if(cache < 0) {
                data[pointer++] = cache;
                if((cache >>> 4 & 0xE) == 0xC) {
                    bteLeft = 1;
                } else if((cache >>> 4 & 0xF) == 0xE) {
                    bteLeft = 2;
                } else if((cache >>> 3 & 0x1F) == 0x1E) {
                    bteLeft = 3;
                } else throw new UTFDataFormatException();
            }
            if(cache > 0) {
                data[pointer++] = cache;
                length--;
            }
            if(pointer == data.length) {
                byte[] che = new byte[data.length+128];
                System.arraycopy(data,0,che,0,data.length);
                data = che;
            }
        }
        return new String(data,0,pointer,StandardCharsets.UTF_8);
    }

    public static UUID readUUID(ByteBuffer buf) {
        return new UUID(buf.getLong(), buf.getLong());
    }

    public static void writeUUID(UUID uid, ByteBuffer buf) {
        buf.putLong(uid.getLeastSignificantBits());
        buf.putLong(uid.getMostSignificantBits());
    }
}
