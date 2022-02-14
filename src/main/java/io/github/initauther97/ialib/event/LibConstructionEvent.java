package io.github.initauther97.ialib.event;

import io.github.initauther97.ialib.IALib;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class LibConstructionEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final IALib instance;

    public LibConstructionEvent(IALib instance) {
        this.instance = instance;
    }

    public IALib getLibrary() {
        return instance;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
