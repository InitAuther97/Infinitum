package org.dhwpcs.infinitum.chat.data;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.dhwpcs.infinitum.Global;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.function.Function;

public class MsgManager {

    private final Path root;
    private final Path hist;

    public static final Function<ChunkMessage, MsgFileInfo> MAPPER
            = cm -> new MsgFileInfo(cm.begin, cm.length, "msg_"+cm.begin+".hst", cm.appendable);
    int maxId = 0;

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

    public void init(MsgHistory hist) throws IOException {
        if (!Files.exists(root)) {
            Files.createDirectories(root);
        }
        Path index = root.resolve("index.json");
        if (!Files.exists(index)) {
            Files.createFile(index);
            JsonObject jo = new JsonObject();
            jo.addProperty("max", 0);
            jo.add("infos", new JsonArray());
            String result = Global.GSON.toJson(jo);
            try(BufferedWriter bw = Files.newBufferedWriter(index)) {
                bw.write(result);
            }
        } else {
            JsonObject jo;
            try(BufferedReader br = Files.newBufferedReader(index)) {
                jo = JsonParser.parseReader(br).getAsJsonObject();
            }
            hist.infos.addAll(new Gson().fromJson(jo.get("infos"),
                    new TypeToken<Set<MsgFileInfo>>() {}.getType()));
            maxId = jo.get("max").getAsInt();
        }
    }

    public void load(MsgHistory hist, int msg) throws MessageFailedException {
        for(MsgFileInfo info : hist.infos) {
            if(msg - info.begin() < 0) {
                throw new MessageFailedException(msg);
            }
            if(msg > info.begin() + info.length()) continue;
            install(hist, info);
        }
    }

    public void install(MsgHistory hist, MsgFileInfo info) throws MessageFailedException {
        Path file = root.resolve("hist").resolve(info.file());
        if(!Files.exists(file)) {
            throw new MessageFailedException(MessageFailedException.Type.CHUNK_NOT_FOUND);
        }
        try {
            hist.run(it -> ChunkMessage.decode(ByteBuffer.wrap(Files.readAllBytes(file))));
        } catch (Exception e) {
            if(e instanceof IOException ex) {
                throw new MessageFailedException(ex, MessageFailedException.Type.CHUNK_NOT_FOUND);
            } else if(e instanceof MessageFailedException ex) {
                throw ex;
            } else if(e instanceof RuntimeException ex) {
                throw ex;
            }
        }
    }

    public void loadMulti(MsgHistory hist, int begin, int end) throws MessageFailedException {
        boolean beginChunk = false;
        for(MsgFileInfo info: hist.infos) {
            if(!beginChunk) {
                if (begin - info.begin() < 0) {
                    throw new MessageFailedException(begin);
                }
                if (begin > info.begin() + info.length()) continue;
                beginChunk = true;
            } else {
                if(end < info.begin()) {
                    break;
                }
                install(hist, info);
            }
        }
    }

    public void release(MsgHistory hist, int begin, int end) throws MessageFailedException{
        if(hist.manager == this) {

        }
    }
}
