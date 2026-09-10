package net.eabbott.meggo;

import net.eabbott.meggo.dataclasses.LevelRenderContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.chunk.LevelChunk;

import java.util.Arrays;
import java.util.stream.Stream;

import static net.eabbott.meggo.Meggo.LOGGER;

public class MeggoManager {
    public static void echo(String text) {
        var minecraft = Minecraft.getInstance();
        var chat = minecraft.gui.hud.getChat();
        chat.addClientSystemMessage(Component.nullToEmpty(text));
    }

    public static void init() {
        LOGGER.info("Starting Meggo on OS: {}", System.getProperty("os.name"));
    }

    public static boolean onClientChatReceived(Component message) {
        String text = message.getString();
        LOGGER.info("Running onClientChatReceived... \"{}\"", text);
        if (text.isEmpty()) return false;

        String[] words = text.split(" ");
        if (
            words.length > 1
            && words[0].startsWith("<")
            && words[0].endsWith(">")
            && words[1].startsWith("\\")
            && words[1].length() > 1
        ) {
            String[] args = Arrays.copyOfRange(words, 1, words.length);
            args[0] = args[0].substring(1);

            echo("Simulating function call with args:");
            for (int i = 0; i < args.length; ++i) {
                echo(String.format("    [%d]: \"%s\"", i, args[i]));
            }

            return true;
        }

        return false;
    }

    public static void setChatScreenInput(EditBox input) {
        LOGGER.info("Running setChatScreenInput...");

    }

    public static void onRenderPassBegin(String string) {
//        LOGGER.info("Running onRenderPassBegin...");

    }

    public static void onKeyboardEvent(int key, int scanCode, int action, int modifiers) {
//        LOGGER.info("Running onKeyboardEvent... {} {} {} {}", key, scanCode, action, modifiers);

    }

    public static void onKeyInput(int key) {
//        LOGGER.info("Running onKeyInput... {}", key);
        var minecraft = Minecraft.getInstance();
        var screen = minecraft.gui.screen();
        if (screen == null && key == '\\') {
            minecraft.gui.setScreen(new ChatScreen("", /* isDraft= */ false));
        }
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
//        LOGGER.info("Running onMouseClick... {} {} {} {} {}", button, action, modifiers, xpos, ypos);

    }

    public static void onChunkLoad(ClientLevel world, LevelChunk chunk) {
//        LOGGER.info("Running onChunkLoad...");

    }

    public static void onChunkUnload(ClientLevel world, LevelChunk chunk) {
//        LOGGER.info("Running onChunkUnload...");

    }

    public static void onClientWorldTick() {
//        LOGGER.info("Running onClientWorldTick...");

    }
}
