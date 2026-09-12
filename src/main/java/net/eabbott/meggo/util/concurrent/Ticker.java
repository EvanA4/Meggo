package net.eabbott.meggo.util.concurrent;

import java.util.concurrent.Phaser;

public class Ticker {
    Phaser phaser;

    public Ticker() {
        phaser = new Phaser();
        phaser.register();
    }

    // Should only be called by the worker threads
    public void await() {
        phaser.register();
        phaser.arriveAndAwaitAdvance();
        phaser.arriveAndDeregister();
    }

    // Should only be called by the clock thread
    public void tick() {
        phaser.arriveAndAwaitAdvance();
    }
}
