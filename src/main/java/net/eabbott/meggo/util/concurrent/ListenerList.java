package net.eabbott.meggo.util.concurrent;

import net.eabbott.meggo.MeggoEvent;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.locks.ReentrantLock;

public class ListenerList {
    private final HashMap<Long, HashMap<MeggoEvent, Long>> pidToListeners = new HashMap<>();
    private final ReentrantLock lock = new ReentrantLock();

    public HashMap<Long, HashMap<MeggoEvent, Long>> snapshot() {
        HashMap<Long, HashMap<MeggoEvent, Long>> output;
        lock.lock();
        try {
            output = new HashMap<>(pidToListeners);

        } finally {
            lock.unlock();
        }
        return output;
    }

    public boolean hasListeners(Long uniqueParentID) {
        boolean output;
        lock.lock();
        try {
            output = pidToListeners.containsKey(uniqueParentID);

        } finally {
            lock.unlock();
        }
        return output;
    }

    public @Nullable Long getListenerID(Long uniqueParentID, MeggoEvent eventType) {
        Long output = null;
        lock.lock();
        try {
            if (
                pidToListeners.containsKey(uniqueParentID)
                && pidToListeners.get(uniqueParentID).containsKey(eventType)
            ) {
                output = pidToListeners.get(uniqueParentID).get(eventType);
            }

        } finally {
            lock.unlock();
        }
        return output;
    }

    public boolean contains(Long uniqueParentID, MeggoEvent eventType) {
        boolean output = false;
        lock.lock();
        try {
            if (pidToListeners.containsKey(uniqueParentID)) {
                output = pidToListeners.get(uniqueParentID).containsKey(eventType);
            }

        } finally {
            lock.unlock();
        }
        return output;
    }

    public void add(Long uniqueParentID, Long uniqueChildID, MeggoEvent eventType) {
        lock.lock();
        try {
            if (!pidToListeners.containsKey(uniqueParentID)) {
                HashMap<MeggoEvent, Long> events = new HashMap<>();
                events.put(eventType, uniqueChildID);
                pidToListeners.put(uniqueParentID, events);
            } else {
                pidToListeners.get(uniqueParentID).put(eventType, uniqueChildID);
            }

        } finally {
            lock.unlock();
        }
    }

    public void remove(Long uniqueParentID, MeggoEvent eventType) {
        lock.lock();
        try {
            if (pidToListeners.containsKey(uniqueParentID)) {
                pidToListeners.get(uniqueParentID).remove(eventType);
                if (pidToListeners.get(uniqueParentID).isEmpty()) {
                    pidToListeners.remove(uniqueParentID);
                }
            }

        } finally {
            lock.unlock();
        }
    }
}
