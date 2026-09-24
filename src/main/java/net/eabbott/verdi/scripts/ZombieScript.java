package net.eabbott.verdi.scripts;

import net.eabbott.verdi.MeggoEvent;
import net.eabbott.verdi.MeggoManager;
import net.eabbott.verdi.MeggoScript;
import net.eabbott.verdi.dataclasses.LevelRenderContext;

public class ZombieScript extends MeggoScript {
    public ZombieScript(Long runnerID, String name) {
        super(runnerID, name);
    }
    private int counter = 0;
    private int toWait = 0;

    @Override
    public void run(String[] args) {
        if (args.length != 2) {
            MeggoManager.print("Usage: \\zombie <seconds>");
            return;
        }

        try {
            toWait = Integer.parseInt(args[1]) * 20;
        } catch (Exception e) {
            MeggoManager.print("Usage: \\zombie <seconds>");
            return;
        }

        addListener(MeggoEvent.CLIENT_WORLD_TICK);
    }

    @Override
    public void render(LevelRenderContext levelRenderContext) {

    }

    @Override
    public boolean isMotor() {
        return false;
    }

    @Override
    public void onClientWorldTick() {
        counter += 1;
        if (counter >= toWait) {
            MeggoManager.print(String.format("Finished being a zombie for %d seconds.", toWait / 20));
            Thread.currentThread().interrupt();
        }
    }
}
