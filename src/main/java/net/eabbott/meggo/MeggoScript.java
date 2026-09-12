package net.eabbott.meggo;

import net.eabbott.meggo.dataclasses.LevelRenderContext;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.chunk.LevelChunk;

public abstract class MeggoScript {
    public Long runnerID;
    public String name;
    public MeggoScript(Long runnerID, String name) { this.runnerID = runnerID; this.name = name; }
    public abstract void run(String[] args);
    public abstract void render(LevelRenderContext levelRenderContext);
    public abstract boolean isMotor();

    public void onClientChatReceived(Component message) {}
    public void setChatScreenInput(EditBox input) {}
    public void onRenderPassBegin(String string) {}
    public void onKeyboardEvent(int key, int scanCode, int action, int modifiers) {}
    public void onKeyInput(int key) {}
    public void onKeyboardKeyPressed(Screen screen, int key) { }
    public void onRenderBegin(LevelRenderContext levelRenderContext) {}
    public void onRenderEnd() {}
    public void onMouseClick(int button, int action, int modifiers, double xpos, double ypos) {}
    public void onChunkLoad(ClientLevel world, LevelChunk chunk) {}
    public void onChunkUnload(ClientLevel world, LevelChunk chunk) {}
    public void onClientWorldTick() {}

    public void addListener(MeggoEvent event) {
        MeggoManager.addListener(runnerID, event);
    }
    public void removeListener(MeggoEvent event) {
        MeggoManager.interruptListener(runnerID, event);
    }
}
