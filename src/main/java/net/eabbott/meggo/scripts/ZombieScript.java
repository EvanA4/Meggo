package net.eabbott.meggo.scripts;

import net.eabbott.meggo.MeggoEvent;
import net.eabbott.meggo.MeggoManager;
import net.eabbott.meggo.MeggoScript;
import net.eabbott.meggo.dataclasses.LevelRenderContext;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.chunk.LevelChunk;

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
