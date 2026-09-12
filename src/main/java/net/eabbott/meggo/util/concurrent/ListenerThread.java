package net.eabbott.meggo.util.concurrent;

import net.eabbott.meggo.MeggoEvent;
import net.eabbott.meggo.MeggoManager;
import net.eabbott.meggo.MeggoScript;
import net.eabbott.meggo.dataclasses.EventArgs;

public class ListenerThread extends Thread {
    Long uniqueParentID;
    MeggoScript script;
    MeggoEvent eventType;

    public ListenerThread(Long uniqueParentID, MeggoScript script, MeggoEvent eventType) {
        this.uniqueParentID = uniqueParentID;
        this.script = script;
        this.eventType = eventType;
    }

    @Override
    public void run() {
        EventArgs args = MeggoManager.getEventArgs(eventType);
        while (args != null && MeggoManager.waitForEvent(eventType) && !Thread.currentThread().isInterrupted()) {
            switch (eventType) {
                case CLIENT_CHAT_RECEIVED -> {
                        script.onClientChatReceived(args.message);
                }
                case CHAT_SCREEN_INPUT -> {
                        script.setChatScreenInput(args.input);
                }
                case RENDER_PASS_BEGIN -> {
                        script.onRenderPassBegin(args.string);
                }
                case KEYBOARD_EVENT -> {
                        script.onKeyboardEvent(args.key, args.scanCode, args.action, args.modifiers);
                }
                case KEY_INPUT -> {
                        script.onKeyInput(args.key);
                }
                case KEYBOARD_KEY_PRESSED -> {
                        script.onKeyboardKeyPressed(args.screen, args.key);
                }
                case RENDER_BEGIN -> {
                        script.onRenderBegin(args.levelRenderContext);
                }
                case RENDER_END -> {
                        script.onRenderEnd();
                }
                case MOUSE_CLICK -> {
                        script.onMouseClick(args.button, args.action, args.modifiers, args.xpos, args.ypos);
                }
                case CHUNK_LOAD -> {
                        script.onChunkLoad(args.world, args.chunk);
                }
                case CHUNK_UNLOAD -> {
                        script.onChunkUnload(args.world, args.chunk);
                }
                case CLIENT_WORLD_TICK -> {
                        script.onClientWorldTick();
                }
            }
        }
        MeggoManager.freeListener(uniqueParentID, eventType);
    }
}