package net.eabbott.meggo;

import net.eabbott.meggo.dataclasses.LevelRenderContext;
import net.eabbott.meggo.util.MeggoUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.chunk.LevelChunk;

import java.util.Arrays;

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
            handleMeggoCommand(args);

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

    private static void handleMeggoCommand(String[] args) {
        echo("Simulating function call with args:");
        for (int i = 0; i < args.length; ++i) {
            echo(String.format("    [%d]: \"%s\"", i, args[i]));
        }

        BlockPos blockPos = MeggoUtil.getTargetedBlock(64);
        if (blockPos != null) {
            echo(String.format(
                    "    Target block: %d %d %d",
                    blockPos.getX(),
                    blockPos.getY(),
                    blockPos.getZ())
            );
        }

        Iterable<Entity> entities = MeggoUtil.getEntities();
        if (entities != null) {
            int ctr = 0;
            for (Entity entity : entities) {
                if (ctr == 5) break;
                echo(String.format("    %s at %f %f %f", entity.getName().toString(), entity.getX(), entity.getY(), entity.getZ()));
                ++ctr;
            }
        }
    }
}
