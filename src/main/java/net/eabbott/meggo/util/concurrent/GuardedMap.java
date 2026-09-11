package net.eabbott.meggo.util.concurrent;

import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

public class GuardedMap<K, V> {
    private final HashMap<K, V> map = new HashMap<>();
    private final ReentrantLock lock = new ReentrantLock();

    public @Nullable V get(K key) {
        V output = null;
        lock.lock();
        try {
            if (map.containsKey(key)) {
                output = map.get(key);
            }
        } finally {
            lock.unlock();
        }
        return output;
    }

    public void put(K key, V value) {
        lock.lock();
        try {
            map.put(key, value);
        } finally {
            lock.unlock();
        }
    }

    public void remove(K key) {
        lock.lock();
        try {
            map.remove(key);
        } finally {
            lock.unlock();
        }
    }

    public Set<K> keySet() {
        Set<K> output = null;
        lock.lock();
        try {
            output = map.keySet();
        } finally {
            lock.unlock();
        }
        return output;
    }

    public boolean containsKey(K key) {
        boolean output;
        lock.lock();
        try {
            output = map.containsKey(key);
        } finally {
            lock.unlock();
        }
        return output;
    }
}
