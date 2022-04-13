package org.dhwpcs.infinitum.statistics;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.dhwpcs.infinitum.Infinitum;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.TemporalQueries;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DailyEventHandler implements Listener {

    final Map<UUID, Long> joins = new HashMap<>();
    private final Map<UUID, Instant> begin = new HashMap<>();
    private final Infinitum plugin;
    private final Daily container;
    private final ReadWriteLock memLock = new ReentrantReadWriteLock();
    private boolean wholeUpdating = false;

    public DailyEventHandler(Infinitum plugin, Daily container) {
        this.plugin = plugin;
        this.container = container;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Instant now = Instant.now();
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> join(event.getPlayer().getUniqueId(), now));
    }

    @EventHandler
    public void onPlayerExit(PlayerQuitEvent event) {
        Instant now = Instant.now();
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> logout(event.getPlayer().getUniqueId(), now));
    }

    private void join(UUID uid, Instant login) {
        memLock.readLock().lock();
        begin.put(uid, login);
        memLock.readLock().unlock();
    }

    private void logout(UUID uid, Instant logout) {
        if(joins.containsKey(uid)) {
            memLock.readLock().lock();
            Instant bg = begin.get(uid);
            LocalDate beforeDate = bg.query(TemporalQueries.localDate());
            LocalDate nowDate = logout.query(TemporalQueries.localDate());
            if (nowDate.isAfter(beforeDate) && !wholeUpdating) {
                wholeUpdating = true;
                memLock.readLock().unlock();
                dayUpdate(beforeDate, nowDate);
                memLock.readLock().lock();
                wholeUpdating = false;
            }
            long secs = calculateTime(logout, bg);
            joins.compute(uid, (unused, val) -> val + secs);
            if(wholeUpdating) {
                begin.remove(uid);
            }
            memLock.readLock().unlock();
        }
    }

    private void dayUpdate(LocalDate beforeDate, LocalDate afterDate) {
        memLock.writeLock().lock();
        Instant instant = Instant.from(afterDate);
        Set<UUID> ks = Set.copyOf(begin.keySet());
        for (UUID key : ks) {
            logout(key, instant);
        }
        container.getRecent().put(beforeDate, Map.copyOf(joins));
        for (UUID key : ks) {
            begin.replace(key, instant);
        }
        memLock.writeLock().unlock();
    }

    private long calculateTime(Instant now, Instant before) {
        return now.getEpochSecond() - before.getEpochSecond();
    }

    public Map<String, Long> getActivities() {
        memLock.writeLock().lock();
        Map<String, Long> joins = new HashMap<>(this.joins.size());
        for(UUID key : this.joins.keySet()) {
            joins.put(Bukkit.getOfflinePlayer(key).getName(), this.joins.get(key));
        }
        memLock.writeLock().unlock();
        return joins;
    }
}
