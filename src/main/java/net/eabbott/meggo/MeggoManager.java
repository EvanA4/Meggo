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

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
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
//        LOGGER.info("Running setChatScreenInput...");
        EventArgs eventArgs = eventArgsMap.get(MeggoEvent.CHAT_SCREEN_INPUT);
        if (eventArgs != null) {
            eventArgs.input = input;
            Ticker signal = listenerLocks.get(MeggoEvent.CHAT_SCREEN_INPUT);
            if (signal != null) signal.tick();
        }
    }

    public static void onRenderPassBegin(String string) {
//        LOGGER.info("Running onRenderPassBegin...");
        EventArgs eventArgs = eventArgsMap.get(MeggoEvent.RENDER_PASS_BEGIN);
        if (eventArgs != null) {
            eventArgs.string = string;
            Ticker signal = listenerLocks.get(MeggoEvent.RENDER_PASS_BEGIN);
            if (signal != null) signal.tick();
        }
    }

    public static void onKeyboardEvent(int key, int scanCode, int action, int modifiers) {
//        LOGGER.info("Running onKeyboardEvent... {} {} {} {}", key, scanCode, action, modifiers);
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
//        LOGGER.info("Running onKeyInput... {}", key);
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
//        LOGGER.info("Running onKeyboardKeyPressed...");
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
//        LOGGER.info("Running onRenderBegin...");
        EventArgs eventArgs = eventArgsMap.get(MeggoEvent.RENDER_BEGIN);
        if (eventArgs != null) {
            eventArgs.levelRenderContext = levelRenderContext;
            Ticker signal = listenerLocks.get(MeggoEvent.RENDER_BEGIN);
            if (signal != null) signal.tick();
        }
    }

    public static void onRenderEnd() {
//        LOGGER.info("Running onRenderEnd...");
        EventArgs eventArgs = eventArgsMap.get(MeggoEvent.RENDER_END);
        if (eventArgs != null) {
            Ticker signal = listenerLocks.get(MeggoEvent.RENDER_END);
            if (signal != null) signal.tick();
        }
    }

    public static void onMouseClick(int button, int action, int modifiers, double xpos, double ypos) {
//        LOGGER.info("Running onMouseClick... {} {} {} {} {}", button, action, modifiers, xpos, ypos);
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
//        LOGGER.info("Running onChunkLoad...");
        EventArgs eventArgs = eventArgsMap.get(MeggoEvent.CHUNK_LOAD);
        if (eventArgs != null) {
            eventArgs.world = world;
            eventArgs.chunk = chunk;
            Ticker signal = listenerLocks.get(MeggoEvent.CHUNK_LOAD);
            if (signal != null) signal.tick();
        }
    }

    public static void onChunkUnload(ClientLevel world, LevelChunk chunk) {
//        LOGGER.info("Running onChunkUnload...");
        EventArgs eventArgs = eventArgsMap.get(MeggoEvent.CHUNK_UNLOAD);
        if (eventArgs != null) {
            eventArgs.world = world;
            eventArgs.chunk = chunk;
            Ticker signal = listenerLocks.get(MeggoEvent.CHUNK_UNLOAD);
            if (signal != null) signal.tick();
        }
    }

    public static void onClientWorldTick() {
//        LOGGER.info("Running onClientWorldTick...");
        chatQueue.flush();

        EventArgs eventArgs = eventArgsMap.get(MeggoEvent.CLIENT_WORLD_TICK);
        if (eventArgs != null) {
            Ticker signal = listenerLocks.get(MeggoEvent.CLIENT_WORLD_TICK);
            if (signal != null) signal.tick();
        }
    }

    private static void handleMeggoCommand(String[] args) {
        // Check manager commands
        if (Objects.equals(args[0], "tasks")) {
            HashMap<Long, HashMap<MeggoEvent, Long>> snapshot = listeners.copy();
            if (snapshot != null) {
                print("Current tasks:");
                for (Long uniqueParentID : snapshot.keySet()) {
                    Task runTask = tasks.get(uniqueParentID);
                    if (runTask != null) {
                        print(String.format("    [%d] %s", uniqueParentID, runTask.script.name));
                        for (MeggoEvent eventType : snapshot.get(uniqueParentID).keySet()) {
                            print(String.format("        [%d] %s", snapshot.get(uniqueParentID).get(eventType), eventType.name()));
                        }
                    }
                }
            }
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
            MeggoScript env = scripts.get(name).getDeclaredConstructor(Long.class, String.class).newInstance(rid, name);
            Task task = Task.createRunTask(rid, Thread.currentThread(), env);
            tasks.put(rid, task);
            env.run(args);

        } catch (Exception e) {
            print(String.format("Failed to start task for \"%s\": %s", name, e.getMessage()));

        } finally {
            tasks.remove(rid);
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

    public static void removeListener(Long uniqueParentID, MeggoEvent eventType) {
        Long lid = listeners.getListenerID(uniqueParentID, eventType);
        listeners.remove(uniqueParentID, eventType);
        Task listenTask = tasks.get(lid);
        if (listenTask != null) {
            Thread lt = listenTask.thread;
            tasks.remove(lid);
            lt.interrupt();
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

    if (blockPos != null) {
        PATH_TO_RENDER = MeggoUtil.getPath(blockPos);
    }




    var color       = ARGB.color(0, 0, 200, 255);
    var fill_color  = ARGB.color(50,  0, 200, 255);
    var filled      = GizmoStyle.strokeAndFill(color, 1.5f, fill_color);

    if (PATH_TO_RENDER != null) {
        for (int i = 0; i < PATH_TO_RENDER.getNodeCount(); ++i) {
            Node node = PATH_TO_RENDER.getNode(i);
            var box = new AABB(new BlockPos(node.x, node.y, node.z));
            Gizmos.cuboid(box, filled).setAlwaysOnTop();
        }
    }
* */

/*
 * render event listeners should execute on main thread
 * scripts should specify whether they're motor scripts
 *      throw error if trying to start a motor script while another is live
 *             that includes any event listeners
 * */