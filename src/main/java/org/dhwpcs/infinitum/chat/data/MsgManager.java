package org.dhwpcs.infinitum.chat.data;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class MsgManager {

    private final Path root;
    private final Path hist;
    private final SortedSet<MsgFileInfo> infos = new TreeSet<>(Comparator.comparingInt(MsgFileInfo::begin));

    public MsgManager(Path root) throws IOException {
        this.root = root;
        if(!Files.exists(root)) {
            Files.createDirectories(root);
        }
        this.hist = root.resolve("hist");
        if(!Files.exists(hist)) {
            Files.createDirectory(hist);
        }
    }

    public void init() throws IOException {
        if(!Files.exists(root)) {
            Files.createDirectories(root);
        }
        Path index = root.resolve("index.json");
        if(!Files.exists(index)) {
            Files.createFile(index);
        }
        infos.addAll(new Gson().fromJson(Files.newBufferedReader(index), new TypeToken<Set<MsgFileInfo>>(){}.getType()));
    }

    public void load(MsgHistory hist, int msg) throws MessageFailedException {
        for(MsgFileInfo info : infos) {
            if(msg - info.begin() < 0) {
                throw new MessageFailedException(msg);
            }
            if(msg > info.begin() + info.length()) continue;
            install(hist, info);
        }
    }

    private void install(MsgHistory hist, MsgFileInfo info) throws MessageFailedException {
        Path file = root.resolve("hist").resolve(info.file());
        if(!Files.exists(file)) {
            throw new MessageFailedException(MessageFailedException.Type.CHUNK_NOT_FOUND);
        }
        try {
            hist.put(ChunkMessage.decode(ByteBuffer.wrap(Files.readAllBytes(file))));
        } catch (IOException e) {
            throw new MessageFailedException(e, MessageFailedException.Type.CHUNK_NOT_FOUND);
        }
    }

    public void loadMulti(MsgHistory hist, int begin, int end) throws MessageFailedException {
        boolean beginChunk = false;
        for(MsgFileInfo info: infos) {
            if(!beginChunk) {
                if (begin - info.begin() < 0) {
                    throw new MessageFailedException(begin);
                }
                if (begin > info.begin() + info.length()) continue;
                beginChunk = true;
            } else {
                if(end > info.begin()) {
                    break;
                }
                install(hist, info);
            }
        }
    }

    public void release(MsgHistory hist) {

    }
}
