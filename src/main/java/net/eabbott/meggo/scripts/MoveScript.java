package net.eabbott.meggo.scripts;

import net.eabbott.meggo.MeggoManager;
import net.eabbott.meggo.MeggoScript;
import net.eabbott.meggo.dataclasses.LevelRenderContext;
import net.eabbott.meggo.util.movement.ForcedClientInput;
import net.eabbott.meggo.util.movement.MeggoInput;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;

public class MoveScript extends MeggoScript {
    public MoveScript(Long runnerID, String name) {
        super(runnerID, name);
    }

    @Override
    public void run(String[] args) {
        if (args.length != 3) {
            MeggoManager.print("Usage: \\move <control> <seconds>");
            return;
        }

        long toWait = -1L;
        MeggoInput input;
        try {
            input = MeggoInput.valueOf(args[1]);
            toWait = Long.parseLong(args[2]) * 1000L;
        } catch (Exception e) {
            MeggoManager.print("Usage: \\move <control> <seconds>");
            return;
        }

        try {
            MeggoManager.setInput(input, true);
            Thread.sleep(toWait);
        } catch (InterruptedException e) {
            MeggoManager.print("Sleeping interrupted!");
        } finally {
            MeggoManager.setInput(input, false);
            MeggoManager.print(String.format("Finished trying to sleep for %d seconds.", toWait / 1000));
        }
    }

    @Override
    public void render(LevelRenderContext levelRenderContext) {

    }

    @Override
    public boolean isMotor() {
        return true;
    }
}
