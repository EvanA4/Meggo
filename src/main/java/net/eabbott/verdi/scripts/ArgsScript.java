package net.eabbott.verdi.scripts;

import net.eabbott.verdi.MeggoManager;
import net.eabbott.verdi.MeggoScript;
import net.eabbott.verdi.dataclasses.LevelRenderContext;

public class ArgsScript extends MeggoScript {
    public boolean isMotor = false;

    public ArgsScript(Long runnerID, String name) {
        super(runnerID, name);
    }

    @Override
    public void run(String[] args) {
        MeggoManager.print("Example script received arguments:");
        for (int i = 0; i < args.length; ++i) {
            MeggoManager.print(String.format("    [%d]: \"%s\"", i, args[i]));
        }
    }

    @Override
    public void render(LevelRenderContext levelRenderContext) {

    }

    @Override
    public boolean isMotor() {
        return false;
    }
}
