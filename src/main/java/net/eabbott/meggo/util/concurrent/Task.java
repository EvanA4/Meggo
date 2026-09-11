package net.eabbott.meggo.util.concurrent;

import net.eabbott.meggo.MeggoScript;

public class Task {
    public Thread thread;
    public MeggoScript script;
    public String role;

    public Task(Thread thread, MeggoScript script, String role) {
         this.thread = thread;
         this.script = script;
         this.role = role;
    }
}
