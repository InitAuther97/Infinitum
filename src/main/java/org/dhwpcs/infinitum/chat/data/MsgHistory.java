package org.dhwpcs.infinitum.chat.data;

import io.github.initauther97.ialib.util.ConsumerWithE;

import java.util.LinkedList;
import java.util.List;

public class MsgHistory {
    private final List<ChunkMessage> message = new LinkedList<>();
    private MsgManager manager;

    public Message getMessage(int id) {
        for(ChunkMessage chunk : message) {
            if(chunk.begin() + chunk.length() < id) {
                continue;
            }
            return chunk.get(id);
        }
        if(manager != null) {

        }
        return null;
    }

    public void setManager(MsgManager manager) {
        this.manager = manager;
    }

    public<E extends Exception> void run(ConsumerWithE<List<ChunkMessage>, E> msgcsm) throws E {
        msgcsm.accept(message);
    }
}
