package net.eabbott.verdi.scripts;

import net.eabbott.verdi.MeggoEvent;
import net.eabbott.verdi.MeggoManager;
import net.eabbott.verdi.MeggoScript;
import net.eabbott.verdi.dataclasses.LevelRenderContext;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.chunk.LevelChunk;

public class ListenScript extends MeggoScript {
    public ListenScript(Long runnerID, String name) {
        super(runnerID, name);
    }

    @Override
    public void run(String[] args) {
        if (args.length != 3) {
            MeggoManager.print("Usage: \\listen <eventType> <seconds>");
            return;
        }

        long toWait;
        MeggoEvent eventType;
        try {
            toWait = Long.parseLong(args[2]) * 1000L;
            eventType = MeggoEvent.valueOf(args[1]);
        } catch (Exception e) {
            MeggoManager.print("Usage: \\sleep <seconds>");
            return;
        }

        try {
            addListener(eventType);
            Thread.sleep(toWait);
        } catch (InterruptedException e) {
            MeggoManager.print("Listening interrupted!");
        } finally {
            removeListener(eventType);
            MeggoManager.print(String.format("Finished listening to %s for %d seconds.", eventType.name(), toWait));
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
    public void onClientChatReceived(Component message) {
        MeggoManager.print("onClientChatReceived");
    }
    @Override
    public void setChatScreenInput(EditBox input) {
        MeggoManager.print("setChatScreenInput");
    }
    @Override
    public void onRenderPassBegin(String string) {
        MeggoManager.print("onRenderPassBegin");
    }
    @Override
    public void onKeyboardEvent(int key, int scanCode, int action, int modifiers) {
        MeggoManager.print("onKeyboardEvent");
    }
    @Override
    public void onKeyInput(int key) {
        MeggoManager.print("onKeyInput");
    }
    @Override
    public void onKeyboardKeyPressed(Screen screen, int key) {
        MeggoManager.print("onKeyboardKeyPressed");
    }
    @Override
    public void onRenderBegin(LevelRenderContext levelRenderContext) {
        MeggoManager.print("onRenderBegin");
    }
    @Override
    public void onRenderEnd() {
        MeggoManager.print("onRenderEnd");
    }
    @Override
    public void onMouseClick(int button, int action, int modifiers, double xpos, double ypos) {
        MeggoManager.print("onMouseClick");
    }
    @Override
    public void onChunkLoad(ClientLevel world, LevelChunk chunk) {
        MeggoManager.print("onChunkLoad");
    }
    @Override
    public void onChunkUnload(ClientLevel world, LevelChunk chunk) {
        MeggoManager.print("onChunkUnload");
    }
    @Override
    public void onClientWorldTick() {
        MeggoManager.print("onClientWorldTick");
    }
}
