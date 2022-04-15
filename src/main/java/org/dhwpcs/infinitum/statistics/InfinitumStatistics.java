package org.dhwpcs.infinitum.statistics;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.initauther97.nugget.file.AccessPermission;
import io.github.initauther97.nugget.file.FileManager;
import io.github.initauther97.nugget.file.type.FolderType;
import io.github.initauther97.nugget.file.type.RegularFileType;
import io.github.initauther97.silverfrost.je.network.BasicCodec;
import org.bukkit.configuration.ConfigurationSection;
import org.dhwpcs.infinitum.Infinitum;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalQueries;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InfinitumStatistics {
    private final Daily daily;
    private final FileManager root;
    private DateTimeFormatter formatter;
    public InfinitumStatistics(Infinitum infinitum) {
        try {
            root = infinitum.getFileManager().visit(FolderType.MANAGER, "statistics");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        daily = new Daily(infinitum);
    }

    public void initialize(ConfigurationSection section) throws IOException {
        Map<LocalDate, Map<UUID, Long>> result = new HashMap<>();
        FileManager activity = root.visit(FolderType.MANAGER, "daily_online");
        BufferedReader reader = activity.visit(RegularFileType.INPUT_READER, "index.json");
        JsonElement je = JsonParser.parseReader(reader);
        reader.close();
        if(je instanceof JsonObject jo) {
            if(jo.has("pattern")) {
                formatter = DateTimeFormatter.ofPattern(jo.getAsString());
                JsonObject entries = jo.getAsJsonObject("entries");
                FileManager data = activity.visit(FolderType.MANAGER, "data");
                LocalDate[] toLoad = new LocalDate[7];
                int pointer = 0;
                LocalDate today = LocalDate.now();
                for(String key : entries.keySet()) {
                    LocalDate date = formatter.parse(key, TemporalQueries.localDate());
                    if(today.toEpochDay() - date.toEpochDay() <= 7) {
                        toLoad[pointer++] = date;
                    }
                }
                for(LocalDate dte : toLoad) {
                    Map<UUID, Long> tms = new HashMap<>();
                    FileChannel fc = data.visit(RegularFileType.FILE_CHANNEL, formatter.format(dte)+".bin");
                    ByteBuffer buffer = ByteBuffer.allocate((int) fc.size());
                    fc.read(buffer);
                    fc.close();
                    buffer.flip();
                    while(buffer.hasRemaining()) {
                        tms.put(BasicCodec.readUUID(buffer), buffer.getLong());
                    }
                    buffer.clear();
                    result.put(dte, tms);
                }
                if(result.containsKey(today)) {
                    Map<UUID, Long> tds = result.remove(today);
                    daily.getListener().joins.putAll(tds);
                }
                daily.setRecent(result);
            }
        } else if(je instanceof JsonNull) {
            daily.setRecent(result);
        } else {
            throw new IOException("Malformed index.json at "+activity.resolve("index.json"));
        }
    }

    public void save(ConfigurationSection section) throws IOException {
        FileManager daily_data = root.visit(FolderType.MANAGER, "daily_online", "data");
        Map<LocalDate, Map<UUID, Long>> dirty = daily.getDirtyRecent();
        for(LocalDate dte : dirty.keySet()) {
            Map<UUID, Long> map = dirty.get(dte);
            FileChannel channel = daily_data.visit(RegularFileType.FILE_CHANNEL, formatter.format(dte) + ".bin");
            for(UUID uid : map.keySet()) {
                ByteBuffer buffer = ByteBuffer.allocate(24);
                BasicCodec.writeUUID(uid, buffer);
                buffer.putLong(map.get(uid));
                buffer.flip();
                channel.write(buffer);
            }
            channel.close();
        }
        daily.getDirtyRecent().clear();
    }

    public FileManager getRoot() {
        return root;
    }

    public Daily getDaily() {
        return daily;
    }
}
