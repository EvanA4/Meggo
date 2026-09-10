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
