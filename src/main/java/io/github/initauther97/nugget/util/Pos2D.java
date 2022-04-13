package io.github.initauther97.nugget.util;

import org.bukkit.Location;

public record Pos2D(int x, int z) {

    public static Pos2D fromString(String s) {
        String[] ss = s.split(",");
        if(ss.length == 2) {
            return new Pos2D(Integer.parseInt(ss[0]), Integer.parseInt(ss[1]));
        }
        return null;
    }

    public static Pos2D flattenLocation(Location l) {
        return new Pos2D(l.getBlockX(), l.getBlockZ());
    }

    public String toString() {
        return String.format("%d,%d", x, z);
    }
}
