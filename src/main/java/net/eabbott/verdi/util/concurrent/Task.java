package net.eabbott.verdi.util.concurrent;

import net.eabbott.verdi.MeggoEvent;
import net.eabbott.verdi.MeggoScript;

public class Task {
    public long uniqueParentID;
    public long uniqueID;
    public Thread thread;
    public MeggoScript script;
    public boolean isRunner;
    public MeggoEvent eventType;

    private Task() {}

    public static Task createRunTask(long uniqueID, Thread thread, MeggoScript script) {
        Task runTask = new Task();
        runTask.uniqueParentID = -1;
        runTask.uniqueID = uniqueID;
        runTask.thread = thread;
        runTask.script = script;
        runTask.isRunner = true;
        runTask.eventType = null;
        return runTask;
    }

    public static Task createListenTask(
        long uniqueParentID, long uniqueID, Thread thread, MeggoScript script, MeggoEvent eventType
    ) {
        Task listenTask = new Task();
        listenTask.uniqueParentID = uniqueParentID;
        listenTask.uniqueID = uniqueID;
        listenTask.thread = thread;
        listenTask.script = script;
        listenTask.isRunner = false;
        listenTask.eventType = eventType;
        return listenTask;
    }
}
