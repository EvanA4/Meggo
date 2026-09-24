package net.eabbott.verdi;

import net.eabbott.verdi.dataclasses.LevelRenderContext;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.chunk.LevelChunk;

public abstract class VerdiScript {
    public boolean isInterrupted = false;
    public void interrupt() { this.isInterrupted = true; }
    public void exit() { VerdiShell.interrupt(); }

    public abstract boolean isMotor();

    public abstract void run(String[] args);

    public void onClientChatReceived(Component message) {}
    public void onSetChatScreenInput(EditBox input) {}
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
}
