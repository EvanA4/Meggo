package net.eabbott.meggo.scripts;

import net.eabbott.meggo.MeggoEvent;
import net.eabbott.meggo.MeggoManager;
import net.eabbott.meggo.MeggoScript;
import net.eabbott.meggo.dataclasses.LevelRenderContext;

public class SleepScript extends MeggoScript {
    public SleepScript(Long runnerID, String name) {
        super(runnerID, name);
    }

    @Override
    public void run(String[] args) {
        if (args.length != 2) {
            MeggoManager.print("Usage: \\sleep <seconds>");
            return;
        }

        long toWait = -1L;
        try {
            toWait = Long.parseLong(args[1]) * 1000L;
        } catch (Exception e) {
            MeggoManager.print("Usage: \\sleep <seconds>");
        }

        try {
            Thread.sleep(toWait);
        } catch (InterruptedException e) {
            MeggoManager.print("Sleeping interrupted!");
        } finally {
            MeggoManager.print(String.format("Finished trying to sleep for %d seconds.", toWait / 1000));
        }
    }

    @Override
    public void render(LevelRenderContext levelRenderContext) {

    }

    @Override
    public boolean isMotor() {
        return false;
    }

    @Override
    public void onMouseClick(int button, int action, int modifiers, double xpos, double ypos) {
        if (button == 0 && action == 1) {
            MeggoManager.print("Registered left mouse click!");
        }
    }
}
