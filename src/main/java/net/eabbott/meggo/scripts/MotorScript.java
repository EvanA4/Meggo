package net.eabbott.meggo.scripts;

import net.eabbott.meggo.MeggoEvent;
import net.eabbott.meggo.MeggoManager;
import net.eabbott.meggo.MeggoScript;
import net.eabbott.meggo.dataclasses.LevelRenderContext;
import net.eabbott.meggo.util.input.PlayerKey;
import net.eabbott.meggo.util.input.PlayerMover;

public class MotorScript extends MeggoScript {
    public MotorScript(Long runnerID, String name) {
        super(runnerID, name);
    }
    private int counter = 0;
    private int zombieTicks = 0;

    @Override
    public void run(String[] args) {
        if (args.length != 3) {
            MeggoManager.print("Usage: \\motor <sleepSeconds> <zombieSeconds>");
            return;
        }

        int sleepSeconds;
        try {
            sleepSeconds = Integer.parseInt(args[1]);
            zombieTicks = Integer.parseInt(args[2]) * 20;
        } catch (Exception e) {
            MeggoManager.print("Usage: \\motor <sleepSeconds> <zombieSeconds>");
            return;
        }

        try {
            Thread.sleep(sleepSeconds * 1000L);

        } catch (InterruptedException e) {
            MeggoManager.print("Motor sleep was interrupted!");
        } finally {
            addListener(MeggoEvent.CLIENT_WORLD_TICK);
        }
    }

    @Override
    public void render(LevelRenderContext levelRenderContext) {

    }

    @Override
    public boolean isMotor() {
        return true;
    }

    @Override
    public void onClientWorldTick() {
        counter += 1;
        if (counter >= zombieTicks) {
            MeggoManager.print(String.format("Finished trying to be a zombie for %d seconds.", zombieTicks / 20));
            Thread.currentThread().interrupt();
        }
    }
}
