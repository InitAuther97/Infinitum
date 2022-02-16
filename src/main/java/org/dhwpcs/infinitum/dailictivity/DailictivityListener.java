package org.dhwpcs.infinitum.dailictivity;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DailictivityListener implements Listener {

    private final Map<String, UUID> joins = new HashMap<>();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        joins.putIfAbsent(event.getPlayer().getName(), event.getPlayer().getUniqueId());
    }

    public Map<String, UUID> pop() {
        try {
            return Map.copyOf(joins);
        } finally {
            joins.clear();
        }
    }
}
