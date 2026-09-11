package net.eabbott.meggo.dataclasses;

import net.eabbott.meggo.MeggoEvent;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.chunk.LevelChunk;

public class EventArgs {
    public Component message = null;
    public EditBox input = null;
    public String string = null;
    public int key = Integer.MIN_VALUE;
    public int scanCode = Integer.MIN_VALUE;
    public int action = Integer.MIN_VALUE;
    public int modifiers = Integer.MIN_VALUE;
    public Screen screen = null;
    public int button = Integer.MIN_VALUE;
    public double xpos = Double.MIN_VALUE;
    public double ypos = Double.MIN_VALUE;
    public ClientLevel world = null;
    public LevelChunk chunk = null;
    public MeggoEvent eventType;
    public LevelRenderContext levelRenderContext = null;

    public EventArgs(MeggoEvent eventType) {
        this.eventType = eventType;
    }
}
