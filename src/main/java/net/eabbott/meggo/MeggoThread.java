package net.eabbott.meggo;

public class MeggoThread extends Thread {
    String name;
    String[] args;

    public MeggoThread(String name, String[] args) {
        this.name = name;
        this.args = args;
    }

    @Override
    public void run() {
        MeggoManager.runScript(name, args);
    }
}

/*
* What do I need?
* - Scripts should not be stored by instance but by class
* - Store threads in hierarchy:
*   - Main thread + dynamic list of event listener threads
* - Script class has abstract boolean canMovePlayer (call these "motor scripts/threads")
*   - Throw error in main thread if trying to call one motor script while currently running another
* - Manager needs a registerListener method
*   - Spawn new thread
*
*
* hashmap
*   - thread ID to script object, and role (ex. tick_listener, runner, etc.)
* killing
*   - killing runner thread should kill all listener threads
*   - killing listener thread should not kill runner thread
*   - TODO: figure out if can kill by TID, or if need thread object in hashmap
*      - if need object, must spawn listener threads creatively without executor?
* registering listeners
*   - runner thread calls registerListener(runnerID, eventName)
*   - executor => threads[rid].script.corresponding
* */