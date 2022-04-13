package org.dhwpcs.infinitum.statistics;

import org.dhwpcs.infinitum.Infinitum;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Daily {
    private final DailyEventHandler listener;
    private final Infinitum instance;
    private Map<LocalDate, Map<UUID, Long>> recent;
    private Map<LocalDate, Map<UUID, Long>> dirty_recent = new HashMap<>();
    public Daily(Infinitum plugin) {
        this.listener = new DailyEventHandler(plugin, this);
        instance = plugin;
    }

    protected void setRecent(Map<LocalDate, Map<UUID, Long>> recent) {
        this.recent = recent;
    }

    public DailyEventHandler getListener() {
        return listener;
    }

    public Map<LocalDate, Map<UUID, Long>> getRecent() {
        return recent;
    }

    public Map<LocalDate, Map<UUID, Long>> getDirtyRecent() {
        return dirty_recent;
    }
}
