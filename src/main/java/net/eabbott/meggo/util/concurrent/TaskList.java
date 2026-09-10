package net.eabbott.meggo.util.concurrent;

import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

public class TaskList {
    private static final HashMap<Long, String> tasks = new HashMap<>();
    private final ReentrantLock lock = new ReentrantLock();

    public @Nullable String get(Long threadID) {
        String output = null;
        lock.lock();
        try {
            if (tasks.containsKey(threadID)) {
                output = tasks.get(threadID);
            }
        } finally {
            lock.unlock();
        }
        return output;
    }

    public void put(Long threadID, String script) {
        lock.lock();
        try {
            tasks.put(threadID, script);
        } finally {
            lock.unlock();
        }
    }

    public void remove(Long threadID) {
        lock.lock();
        try {
            tasks.remove(threadID);
        } finally {
            lock.unlock();
        }
    }

    public Set<Long> keySet() {
        Set<Long> output = null;
        lock.lock();
        try {
            output = tasks.keySet();
        } finally {
            lock.unlock();
        }
        return output;
    }
}
