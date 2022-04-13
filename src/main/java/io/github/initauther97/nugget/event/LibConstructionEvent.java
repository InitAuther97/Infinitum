package io.github.initauther97.nugget.event;

import io.github.initauther97.nugget.NuggetLib;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class LibConstructionEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final NuggetLib instance;

    public LibConstructionEvent(NuggetLib instance) {
        this.instance = instance;
    }

    public NuggetLib getLibrary() {
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
