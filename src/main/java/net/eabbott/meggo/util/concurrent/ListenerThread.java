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
                        MeggoManager.onClientChatReceived(args.message);
                }
                case CHAT_SCREEN_INPUT -> {
                        MeggoManager.setChatScreenInput(args.input);
                }
                case RENDER_PASS_BEGIN -> {
                        MeggoManager.onRenderPassBegin(args.string);
                }
                case KEYBOARD_INPUT -> {
                        MeggoManager.onKeyboardEvent(args.key, args.scanCode, args.action, args.modifiers);
                }
                case KEY_INPUT -> {
                        MeggoManager.onKeyInput(args.key);
                }
                case KEYBOARD_KEY_PRESSED -> {
                        MeggoManager.onKeyboardKeyPressed(args.screen, args.key);
                }
                case RENDER_BEGIN -> {
                        MeggoManager.onRenderBegin(args.levelRenderContext);
                }
                case RENDER_END -> {
                        MeggoManager.onRenderEnd();
                }
                case MOUSE_CLICK -> {
                        MeggoManager.onMouseClick(args.button, args.action, args.modifiers, args.xpos, args.ypos);
                }
                case CHUNK_LOAD -> {
                        MeggoManager.onChunkLoad(args.world, args.chunk);
                }
                case CHUNK_UNLOAD -> {
                        MeggoManager.onChunkUnload(args.world, args.chunk);
                }
                case CLIENT_WORLD_TICK -> {
                        MeggoManager.onClientWorldTick();
                }
            }
        }

        // TODO: cleanup thread here
    }
}