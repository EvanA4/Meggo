package net.eabbott.meggo;

import net.eabbott.meggo.dataclasses.LevelRenderContext;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.chunk.LevelChunk;

import static net.eabbott.meggo.Meggo.LOGGER;

public class MeggoManager {
    public static void init() {
        LOGGER.info("Starting Meggo on OS: {}", System.getProperty("os.name"));
    }

    public static boolean onClientChatReceived(Component message) {
        LOGGER.info("Running onClientChatReceived...");
        return false;
    }

    public static void setChatScreenInput(EditBox input) {
        LOGGER.info("Running setChatScreenInput...");

    }

    public static void onRenderPassBegin(String string) {
//        LOGGER.info("Running onRenderPassBegin...");

    }

    public static void onKeyboardEvent(int key, int scanCode, int action, int modifiers) {
        LOGGER.info("Running onKeyboardEvent... {} {} {} {}", key, scanCode, action, modifiers);

    }

    public static void onKeyInput(int key) {
        LOGGER.info("Running onKeyInput... {}", key);

    }

    public static boolean onKeyboardKeyPressed(Screen screen, int key) {
        LOGGER.info("Running onKeyboardKeyPressed...");
        return false;
    }

    public static void onRenderBegin(LevelRenderContext levelRenderContext) {
//        LOGGER.info("Running onRenderBegin...");

    }

    public static void onRenderEnd() {
//        LOGGER.info("Running onRenderEnd...");

    }

    public static void onMouseClick(int button, int action, int modifiers, double xpos, double ypos) {
        LOGGER.info("Running onMouseClick... {} {} {} {} {}", button, action, modifiers, xpos, ypos);

    }

    public static void onChunkLoad(ClientLevel world, LevelChunk chunk) {
        LOGGER.info("Running onChunkLoad...");

    }

    public static void onChunkUnload(ClientLevel world, LevelChunk chunk) {
        LOGGER.info("Running onChunkUnload...");

    }

    public static void onClientWorldTick() {
//        LOGGER.info("Running onClientWorldTick...");

    }
}
