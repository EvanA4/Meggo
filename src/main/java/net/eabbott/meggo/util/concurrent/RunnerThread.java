package net.eabbott.meggo.util.concurrent;

import net.eabbott.meggo.MeggoManager;

public class RunnerThread extends Thread {
    String name;
    String[] args;

    public RunnerThread(String name, String[] args) {
        this.name = name;
        this.args = args;
    }

    @Override
    public void run() {
        MeggoManager.runScript(name, args);
    }
}