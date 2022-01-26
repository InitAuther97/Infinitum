package org.dhwpcs.infinitum.chat.data;

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
        return null;
    }

    public void setManager(MsgManager manager) {
        this.manager = manager;
    }

    public void put(ChunkMessage messageIn) {
        message.add(messageIn);
    }
}
