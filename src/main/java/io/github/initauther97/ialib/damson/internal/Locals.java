package io.github.initauther97.ialib.damson.internal;

import io.github.initauther97.ialib.key.Key;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({"rawtypes","unchecked"})
public class Locals {

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

    public static<T> Object get(Key<?> local, Object key) {
        Map ll = LOCAL_POOL.get(local);
        if(ll == null) {
            throw new IllegalArgumentException("Not registered local!");
        }
        Object result = ll.get(key);
        if(result == null) {
            throw new IllegalArgumentException("Entry not present!");
        }
        return ll;
    }

    public static boolean present(Key<?> local, Object key) {
        Map ll = LOCAL_POOL.get(local);
        if(ll == null) {
            throw new IllegalArgumentException("Not registered local!");
        }
        return ll.containsKey(key);
    }
}
