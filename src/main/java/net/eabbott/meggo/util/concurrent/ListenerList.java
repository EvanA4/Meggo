package net.eabbott.meggo.util.concurrent;

import net.eabbott.meggo.MeggoEvent;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.locks.ReentrantLock;

public class ListenerList {
    private final HashMap<Long, HashSet<MeggoEvent>> idToEventTypes = new HashMap<>();
    private final HashMap<MeggoEvent, HashSet<Long>> eventTypeToIDs = new HashMap<>();
    private final ReentrantLock lock = new ReentrantLock();
    
    public boolean contains(Long threadID, MeggoEvent eventType) {
        boolean output = false;
        lock.lock();
        try {
            if (idToEventTypes.containsKey(threadID)) {
                output = idToEventTypes.get(threadID).contains(eventType);
            }

        } finally {
            lock.unlock();
        }
        return output;
    }

    public @Nullable HashSet<MeggoEvent> getByID(Long threadID) {
        HashSet<MeggoEvent> output = null;
        lock.lock();
        try {
            if (idToEventTypes.containsKey(threadID)) {
                output = idToEventTypes.get(threadID);
            }

        } finally {
            lock.unlock();
        }
        return output;
    }

    public @Nullable HashSet<Long> getByEvent(MeggoEvent event) {
        HashSet<Long> output = null;
        lock.lock();
        try {
            if (eventTypeToIDs.containsKey(event)) {
                output = eventTypeToIDs.get(event);
            }

        } finally {
            lock.unlock();
        }
        return output;
    }

    public void add(Long threadID, MeggoEvent event) {
        lock.lock();
        try {
            if (!idToEventTypes.containsKey(threadID)) {
                HashSet<MeggoEvent> events = new HashSet<>();
                events.add(event);
                idToEventTypes.put(threadID, events);
            } else {
                idToEventTypes.get(threadID).add(event);
            }

            if (!eventTypeToIDs.containsKey(event)) {
                HashSet<Long> ids = new HashSet<>();
                ids.add(threadID);
                eventTypeToIDs.put(event, ids);
            } else {
                eventTypeToIDs.get(event).add(threadID);
            }

        } finally {
            lock.unlock();
        }
    }

    public void remove(Long threadID) {
        lock.lock();
        try {
            if (idToEventTypes.containsKey(threadID)) {
                for (MeggoEvent event : idToEventTypes.get(threadID)) {
                    if (eventTypeToIDs.containsKey(event)) {
                        eventTypeToIDs.get(event).remove(threadID);
                    }
                }
            }

        } finally {
            lock.unlock();
        }
    }
}
