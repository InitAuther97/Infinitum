package org.dhwpcs.infinitum.chat.data;

import io.github.initauther97.ialib.util.ConsumerWithE;

import java.io.IOException;
import java.util.*;

public class MsgHistory {
    private final List<ChunkMessage> message = new LinkedList<>();
    final SortedSet<MsgFileInfo> infos = new TreeSet<>(Comparator.comparingInt(MsgFileInfo::begin));
    int maxId;
    private MsgManager manager;

    public MsgHistory(MsgManager manager) throws IOException {
        this.manager = manager;
        manager.init(this);
        maxId = manager.maxId;
    }

    public Message appendMessage(Message.Builder builder) {
        Message msg = builder.build(maxId++);
        ChunkMessage cm = message.get(message.size() - 1);
        if(cm.appendable) {
            cm.append(msg);
        } else {
            ChunkMessage nm = new ChunkMessage(maxId);
            infos.add(MsgManager.MAPPER.apply(nm));
            nm.append(msg);
            message.add(nm);
        }
        return msg;
    }

    public Message getMessage(int id) {
        for(ChunkMessage chunk : message) {
            if(chunk.begin + chunk.length < id) {
                continue;
            }
            return chunk.get(id);
        }
        if(manager != null) {
            try {
                manager.load(this, id);
                return getMessage(id);
            } catch (MessageFailedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public<E extends Exception> void run(ConsumerWithE<List<ChunkMessage>, E> msgcsm) throws E {
        msgcsm.accept(message);
    }
}
