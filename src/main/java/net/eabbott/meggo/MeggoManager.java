package net.eabbott.meggo;

import net.eabbott.meggo.dataclasses.EventArgs;
import net.eabbott.meggo.dataclasses.LevelRenderContext;
import net.eabbott.meggo.util.concurrent.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.chunk.LevelChunk;
import org.jspecify.annotations.Nullable;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

import static net.eabbott.meggo.Meggo.LOGGER;

public class MeggoManager {
    private static final HashMap<String, Class<? extends MeggoScript>> scripts = new HashMap<>();
    private static final GuardedMap<Long, Task> tasks = new GuardedMap<>();
    private static final ChatQueue chatQueue = new ChatQueue();
    private static final ListenerList listeners = new ListenerList();
    private static final GuardedMap<MeggoEvent, Ticker> listenerLocks = new GuardedMap<>();
    private static final GuardedMap<MeggoEvent, EventArgs> eventArgsMap = new GuardedMap<>();
    private static final AtomicLong idGenerator = new AtomicLong(0);
    private static final HashSet<Long> runnerIDs = new HashSet<>();
    private static Long motorID = -1L;

    public static void print(String text) {
        chatQueue.push(text);
    }

    public static void init() {
        LOGGER.info("Starting Meggo on OS: {}", System.getProperty("os.name"));
        for (MeggoEvent eventType : MeggoEvent.values()) {
            eventArgsMap.put(eventType, new EventArgs(eventType));
            listenerLocks.put(eventType, new Ticker());
        }
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

        EventArgs eventArgs = eventArgsMap.get(MeggoEvent.CLIENT_CHAT_RECEIVED);
        if (eventArgs != null) {
            eventArgs.message = message;
            Ticker signal = listenerLocks.get(MeggoEvent.CLIENT_CHAT_RECEIVED);
            if (signal != null) signal.tick();
        }
        return false;
    }

    public static void setChatScreenInput(EditBox input) {
        EventArgs eventArgs = eventArgsMap.get(MeggoEvent.CHAT_SCREEN_INPUT);
        if (eventArgs != null) {
            eventArgs.input = input;
            Ticker signal = listenerLocks.get(MeggoEvent.CHAT_SCREEN_INPUT);
            if (signal != null) signal.tick();
        }
    }

    public static void onRenderPassBegin(String string) {
        EventArgs eventArgs = eventArgsMap.get(MeggoEvent.RENDER_PASS_BEGIN);
        if (eventArgs != null) {
            eventArgs.string = string;
            Ticker signal = listenerLocks.get(MeggoEvent.RENDER_PASS_BEGIN);
            if (signal != null) signal.tick();
        }
    }

    public static void onKeyboardEvent(int key, int scanCode, int action, int modifiers) {
        EventArgs eventArgs = eventArgsMap.get(MeggoEvent.KEYBOARD_EVENT);
        if (eventArgs != null) {
            eventArgs.key = key;
            eventArgs.scanCode = scanCode;
            eventArgs.action = action;
            eventArgs.modifiers = modifiers;
            Ticker signal = listenerLocks.get(MeggoEvent.KEYBOARD_EVENT);
            if (signal != null) signal.tick();
        }
    }

    public static void onKeyInput(int key) {
        var minecraft = Minecraft.getInstance();
        var screen = minecraft.gui.screen();
        if (screen == null && key == '\\') {
            minecraft.gui.setScreen(new ChatScreen("", /* isDraft= */ false));
        }

        EventArgs eventArgs = eventArgsMap.get(MeggoEvent.KEY_INPUT);
        if (eventArgs != null) {
            eventArgs.key = key;
            Ticker signal = listenerLocks.get(MeggoEvent.KEY_INPUT);
            if (signal != null) signal.tick();
        }
    }

    public static boolean onKeyboardKeyPressed(Screen screen, int key) {
        EventArgs eventArgs = eventArgsMap.get(MeggoEvent.KEYBOARD_KEY_PRESSED);
        if (eventArgs != null) {
            eventArgs.screen = screen;
            eventArgs.key = key;
            Ticker signal = listenerLocks.get(MeggoEvent.KEYBOARD_KEY_PRESSED);
            if (signal != null) signal.tick();
        }

        return false;
    }

    public static void onRenderBegin(LevelRenderContext levelRenderContext) {
        for (Long runnerID : runnerIDs) {
            Task runTask = tasks.get(runnerID);
            if (runTask != null) {
                runTask.script.render(levelRenderContext);
            }
        }

        EventArgs eventArgs = eventArgsMap.get(MeggoEvent.RENDER_BEGIN);
        if (eventArgs != null) {
            eventArgs.levelRenderContext = levelRenderContext;
            Ticker signal = listenerLocks.get(MeggoEvent.RENDER_BEGIN);
            if (signal != null) signal.tick();
        }
    }

    public static void onRenderEnd() {
        EventArgs eventArgs = eventArgsMap.get(MeggoEvent.RENDER_END);
        if (eventArgs != null) {
            Ticker signal = listenerLocks.get(MeggoEvent.RENDER_END);
            if (signal != null) signal.tick();
        }
    }

    public static void onMouseClick(int button, int action, int modifiers, double xpos, double ypos) {
        EventArgs eventArgs = eventArgsMap.get(MeggoEvent.MOUSE_CLICK);
        if (eventArgs != null) {
            eventArgs.button = button;
            eventArgs.action = action;
            eventArgs.modifiers = modifiers;
            eventArgs.xpos = xpos;
            eventArgs.ypos = ypos;
            Ticker signal = listenerLocks.get(MeggoEvent.MOUSE_CLICK);
            if (signal != null) signal.tick();
        }
    }

    public static void onChunkLoad(ClientLevel world, LevelChunk chunk) {
        EventArgs eventArgs = eventArgsMap.get(MeggoEvent.CHUNK_LOAD);
        if (eventArgs != null) {
            eventArgs.world = world;
            eventArgs.chunk = chunk;
            Ticker signal = listenerLocks.get(MeggoEvent.CHUNK_LOAD);
            if (signal != null) signal.tick();
        }
    }

    public static void onChunkUnload(ClientLevel world, LevelChunk chunk) {
        EventArgs eventArgs = eventArgsMap.get(MeggoEvent.CHUNK_UNLOAD);
        if (eventArgs != null) {
            eventArgs.world = world;
            eventArgs.chunk = chunk;
            Ticker signal = listenerLocks.get(MeggoEvent.CHUNK_UNLOAD);
            if (signal != null) signal.tick();
        }
    }

    public static void onClientWorldTick() {
        chatQueue.flush();

        EventArgs eventArgs = eventArgsMap.get(MeggoEvent.CLIENT_WORLD_TICK);
        if (eventArgs != null) {
            Ticker signal = listenerLocks.get(MeggoEvent.CLIENT_WORLD_TICK);
            if (signal != null) signal.tick();
        }
    }

    private static void printTasks() {
        HashMap<Long, HashMap<MeggoEvent, Long>> snapshot = listeners.snapshot();
        HashSet<Long> deadRunners = new HashSet<>(snapshot.keySet());
        deadRunners.removeAll(runnerIDs);

        // Display living runners first
        print("Current tasks:");
        for (Long runnerID : runnerIDs) {
            Task runTask = tasks.get(runnerID);
            if (runTask != null) {
                print(String.format("    [%d] %s", runnerID, runTask.script.name));
                if (snapshot.containsKey(runnerID)) {
                    for (MeggoEvent eventType : snapshot.get(runnerID).keySet()) {
                        print(String.format(
                            "        [%d] %s", snapshot.get(runnerID).get(eventType), eventType.name())
                        );
                    }
                }
            }
        }

        // Display zombies after
        for (Long deadRunnerID : deadRunners) {
            print(String.format("    [%d] %s", deadRunnerID, "terminated"));
            for (MeggoEvent eventType : snapshot.get(deadRunnerID).keySet()) {
                print(String.format(
                    "        [%d] %s", snapshot.get(deadRunnerID).get(eventType), eventType.name())
                );
            }
        }
    }

    private static void interruptThread(Long uniqueID) {
        Task task = tasks.get(uniqueID);
        if (task != null) {
            task.thread.interrupt();
        } else {
            print(String.format("Warning: task does not exist with ID %d", uniqueID));
        }
    }

    private static void help() {
        print("Built-in commands:");
        print("    \\tasks");
        print("        Prints the running threads to the client-side chat.");
        print("    \\interrupt <threadID>");
        print("        Interrupts the thread of the same ID, asking it to quit safely.");
        print("    \\help");
        print("        Prints the available commands/scripts in client-side chat.");

        print("Scripts:");
        for (String name : scripts.keySet()) {
            print(String.format("    %s", name));
        }
    }

    private static void handleMeggoCommand(String[] args) {
        // Check manager commands
        if (Objects.equals(args[0], "tasks")) {
            printTasks();

        } else if (Objects.equals(args[0], "interrupt")) {
            if (args.length != 2) {
                print("usage: \\interrupt <threadID>");
            } else {
                try {
                    interruptThread(Long.parseLong(args[1]));
                } catch (Exception e) {
                    print("usage: \\interrupt <threadID>");
                }
            }

        } else if (Objects.equals(args[0], "help")) {
            help();
        }

        // Check non-manager commands
        else if (!scripts.containsKey(args[0])) {
            print(String.format("Command not recognized: \"%s\"", args[0]));

        } else {
            RunnerThread task = new RunnerThread(args[0], args);
            task.start();
        }
    }

    public static void addScript(String name, Class<? extends MeggoScript> script) {
        scripts.put(name, script);
    }

    // FUNCTIONS BELOW CAN ONLY BE RUN BY SECONDARY THREADS

    public static void runScript(String name, String[] args) {
        long rid = idGenerator.incrementAndGet();
        try {
            MeggoScript env = scripts.get(name)
                    .getDeclaredConstructor(Long.class, String.class).newInstance(rid, name);

            // check for motor collision
            if (env.isMotor()) {
                if (
                    motorID == -1 || !(tasks.containsKey(motorID) || listeners.hasListeners(motorID))
                ) {
                    motorID = rid;
                } else {
                    // motor collision detected
                    throw new Exception("Motor collision detected!");
                }
            }

            Task task = Task.createRunTask(rid, Thread.currentThread(), env);
            tasks.put(rid, task);
            runnerIDs.add(rid);
            env.run(args);

        } catch (Exception e) {
            print(String.format("Failed to start task for \"%s\": %s", name, e.getMessage()));

        } finally {
            tasks.remove(rid);
            runnerIDs.remove(rid);
        }
    }

    public static void addListener(Long uniqueParentID, MeggoEvent eventType) {
        Task parentTask = tasks.get(uniqueParentID);
        if (parentTask != null && !listeners.contains(uniqueParentID, eventType)) {
            long lid = idGenerator.incrementAndGet();
            listeners.add(uniqueParentID, lid, eventType);
            ListenerThread lt = new ListenerThread(uniqueParentID, parentTask.script, eventType);
            Task childTask = Task.createListenTask(
                uniqueParentID, lid, lt, parentTask.script, eventType
            );
            tasks.put(lid, childTask);
            lt.start();
        }
    }

    public static void interruptListener(Long uniqueParentID, MeggoEvent eventType) {
        Long lid = listeners.getListenerID(uniqueParentID, eventType);
        if (lid != null) {
            Task listenTask = tasks.get(lid);
            if (listenTask != null) {
                Thread lt = listenTask.thread;
                lt.interrupt();
            }
        }
    }

    public static void freeListener(Long uniqueParentID, MeggoEvent eventType) {
        Long lid = listeners.getListenerID(uniqueParentID, eventType);
        if (lid != null) {
            listeners.remove(uniqueParentID, eventType);
            tasks.remove(lid);
        }
    }

    public static boolean waitForEvent(MeggoEvent eventType) {
        try {
            Ticker signal = listenerLocks.get(eventType);
            if (signal != null) {
                signal.await();
                return true;
            }
            return false;

        } catch (Exception e) {
            return false;
        }
    }

    public static @Nullable EventArgs getEventArgs(MeggoEvent eventType) {
        return eventArgsMap.get(eventType);
    }
}

/*
 * render functions should execute on main thread
 * */