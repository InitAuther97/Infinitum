package org.dhwpcs.infinitum.dailictivity;

import org.bukkit.plugin.Plugin;

public class Daily {
    private final DailictivityListener listener;

    public Daily(Plugin plugin) {
        this.listener = new DailictivityListener();
    }

    public DailictivityListener getListener() {
        return listener;
    }
}
