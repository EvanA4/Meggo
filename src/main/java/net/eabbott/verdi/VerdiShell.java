package net.eabbott.verdi;

import net.eabbott.verdi.dataclasses.LevelRenderContext;
import net.eabbott.verdi.util.VerdiChat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.chunk.LevelChunk;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class VerdiShell {
    public static final Logger LOGGER = LoggerFactory.getLogger("Verdi");
    private static final HashMap<String, Class<? extends VerdiScript>> scripts = new HashMap<>();
    private static @Nullable VerdiScript currentTask = null;
    private static boolean isInputLocked = false;

    public static boolean onClientChatReceived(Component message) {
        String text = message.getString();
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

        if (currentTask != null) currentTask.onClientChatReceived(message);
        return false;
    }

    public static void onKeyInput(int key) {
        var minecraft = Minecraft.getInstance();
        var screen = minecraft.gui.screen();
        if (screen == null && key == '\\') {
            minecraft.gui.setScreen(new ChatScreen("", false));
        }

        if (currentTask != null) currentTask.onKeyInput(key);
    }

    public static void interrupt() {
        if (currentTask != null) currentTask.interrupt();
        toggleMovement(false);
        currentTask = null;
    }

    private static void help() {
        VerdiChat.send("Built-in commands:");
        VerdiChat.send("    \\status");
        VerdiChat.send("        Prints whether a task is currently running.");
        VerdiChat.send("    \\interrupt");
        VerdiChat.send("        Interrupts the currently running task, asking it to quit safely.");
        VerdiChat.send("    \\help");
        VerdiChat.send("        Prints the available commands/scripts in client-side chat.");

        VerdiChat.send("Scripts:");
        for (String name : scripts.keySet()) {
            VerdiChat.send(String.format("    %s", name));
        }
    }

    private static void handleMeggoCommand(String[] args) {
        // Check manager commands
        if (args[0].equals("status")) {
            if (currentTask != null) VerdiChat.send("A task is currently running.");
            else VerdiChat.send("No task is running.");

        } else if (args[0].equals("interrupt")) {
            if (args.length != 1) {
                VerdiChat.send("usage: \\interrupt");
            } else {
                try {
                    interrupt();
                } catch (Exception e) {
                    VerdiChat.send("usage: \\interrupt <threadID>");
                }
            }

        } else if (Objects.equals(args[0], "help")) {
            help();
        }

        // Check non-manager commands
        else if (currentTask != null) {
            VerdiChat.send("Error: A task is already running.");

        } else if (!scripts.containsKey(args[0])) {
            VerdiChat.send("Command not recognized: \"%s\"", args[0]);

        } else {
            try {
                currentTask = scripts.get(args[0]).getDeclaredConstructor().newInstance();
                if (currentTask.isMotor()) toggleMovement(true);
                currentTask.run(args);

            } catch (Exception e) {
                VerdiChat.send("Failed to start task: %s", e.getMessage());
            }
        }
    }

    public static void addScript(String name, Class<? extends VerdiScript> script) {
        try {
            scripts.put(name, script);
        } catch (Exception _) {}
    }

    public static boolean getIsInputLocked() {
        return isInputLocked;
    }

    private static void toggleMovement(boolean locked) {
        isInputLocked = locked;
    }

    // ENDLESS SEA OF PROXY EVENT LISTENERS

    public static void setChatScreenInput(EditBox input) {
        if (currentTask != null) currentTask.onSetChatScreenInput(input);
    }

    public static void onRenderPassBegin(String string) {
        if (currentTask != null) currentTask.onRenderPassBegin(string);
    }

    public static void onKeyboardEvent(int key, int scanCode, int action, int modifiers) {
        if (currentTask != null) currentTask.onKeyboardEvent(key, scanCode, action, modifiers);
    }

    public static void onKeyboardKeyPressed(Screen screen, int key) {
        if (currentTask != null) currentTask.onKeyboardKeyPressed(screen, key);
    }

    public static void onRenderBegin(LevelRenderContext levelRenderContext) {
        if (currentTask != null) currentTask.onRenderBegin(levelRenderContext);
    }

    public static void onRenderEnd() {
        if (currentTask != null) currentTask.onRenderEnd();
    }

    public static void onMouseClick(int button, int action, int modifiers, double xpos, double ypos) {
        if (currentTask != null) currentTask.onMouseClick(button, action, modifiers, xpos, ypos);
    }

    public static void onChunkLoad(ClientLevel world, LevelChunk chunk) {
        if (currentTask != null) currentTask.onChunkLoad(world, chunk);
    }

    public static void onChunkUnload(ClientLevel world, LevelChunk chunk) {
        if (currentTask != null) currentTask.onChunkUnload(world, chunk);
    }

    public static void onClientWorldTick() {
        if (currentTask != null) currentTask.onClientWorldTick();
    }
}

/*
 * create functions to move player
 * */