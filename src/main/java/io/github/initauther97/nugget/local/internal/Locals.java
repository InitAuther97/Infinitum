package io.github.initauther97.nugget.local.internal;

import io.github.initauther97.nugget.key.Key;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({"rawtypes","unchecked"})
public abstract class Locals {

    private Locals() {throw new UnsupportedOperationException();}

    private static final Map<Key, Map> LOCAL_POOL = new HashMap<>();

    public static void allocate(Key key) {
        if(key.runtimeType().isEnum()) {
            LOCAL_POOL.put(key, new EnumMap((Class<Enum>) key.runtimeType()));
        } else {
            LOCAL_POOL.put(key, new HashMap());
        }
    }

    public static void put(Key<?> local, Object key, Object value) {
        Map ll = LOCAL_POOL.get(local);
        if(ll == null) {
            throw new IllegalArgumentException("Not registered local!");
        }
        ll.put(key, value);
    }

    public static Object get(Key<?> local, Object key) {
        Map ll = LOCAL_POOL.get(local);
        if(ll == null) {
            throw new IllegalArgumentException("Not registered local!");
        }
        return ll.get(key);
    }

    public static boolean present(Key<?> local, Object key) {
        Map ll = LOCAL_POOL.get(local);
        if(ll == null) {
            throw new IllegalArgumentException("Not registered local!");
        }
        return ll.containsKey(key);
    }
}
