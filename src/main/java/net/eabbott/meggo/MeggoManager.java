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
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

import static net.eabbott.meggo.Meggo.LOGGER;

public class MeggoManager {
    private static final HashMap<String, Class<? extends MeggoScript>> scripts = new HashMap<>();
    private static final GuardedMap<Long, Task> tasks = new GuardedMap<>();
    private static final ChatQueue chatQueue = new ChatQueue();
    private static final ListenerList listeners = new ListenerList();
    private static final GuardedMap<MeggoEvent, CountDownLatch> listenerLocks = new GuardedMap<>();
    private static final GuardedMap<MeggoEvent, EventArgs> eventArgsMap = new GuardedMap<>();

    public static void print(String text) {
        chatQueue.push(text);
    }

    public static void init() {
        LOGGER.info("Starting Meggo on OS: {}", System.getProperty("os.name"));
        for (MeggoEvent eventType : MeggoEvent.values()) {
            eventArgsMap.put(eventType, new EventArgs(eventType));
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
        chatQueue.flush();
    }

    private static void handleMeggoCommand(String[] args) {
        // Check manager commands
        if (Objects.equals(args[0], "tasks")) {
            print("Current tasks:");
            for (Long i : tasks.keySet()) {
                print(String.format("    [%d] %s", i, tasks.get(i)));
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

    public static void addScript(Class<? extends MeggoScript> script) {
        scripts.put(script.getName(), script);
    }

    // FUNCTIONS BELOW CAN ONLY BE RUN BY SECONDARY THREADS

    public static void runScript(String name, String[] args) {
        try {
            MeggoScript env = scripts.get(name).getDeclaredConstructor().newInstance();
            Task task = new Task(Thread.currentThread(), env, "runner");
            tasks.put(Thread.currentThread().threadId(), task);
            env.run(args);

        } catch (Exception e) {
            print(String.format("Failed to start task for \"%s\": %s", name, e.getMessage()));

        } finally {
            Long threadID = Thread.currentThread().threadId();
            tasks.remove(threadID);
            listeners.remove(threadID);
        }
    }

    public static void addListener(Long parentID, MeggoEvent event) {
        if (!listeners.contains(parentID, event)) {
            listeners.add(parentID, event);

            ListenerThread task = new ListenerThread();
            task.start();
        }
    }

    public static boolean waitForEvent(MeggoEvent eventType) {
        try {
            CountDownLatch signal = listenerLocks.get(eventType);
            if (signal != null) {
                signal.await();
                return true;
            }
            return false;

        } catch (Exception e) {
            return false;
        }
    }

    public static @Nullable MeggoScript getScriptEnv(Long threadID) {
        Task task = tasks.get(threadID);
        return task == null ? null : task.script;
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
 * What do I need?
 * - Scripts should not be stored by instance but by class
 * - Store threads in hierarchy:
 *   - Main thread + dynamic list of event listener threads
 * - Script class has abstract boolean canMovePlayer (call these "motor scripts/threads")
 *   - Throw error in main thread if trying to call one motor script while currently running another
 * - Manager needs a registerListener method
 *   - Spawn new thread
 *
 *
 * hashmap
 *   - thread ID to script object, and role (ex. tick_listener, runner, etc.)
 * killing
 *   - killing runner thread should kill all listener threads
 *   - killing listener thread should not kill runner thread
 *   - figure out if can kill by TID, or if need thread object in hashmap
 *      - if need object, must spawn listener threads creatively without executor?
 * registering listeners
 *   - runner thread calls registerListener(runnerID, eventName)
 *   - executor => threads[rid].script.corresponding
 *
 * listeners need to be signaled, use CountDownLatch signal = new CountDownLatch(1);
 *  - might need shared memory
 *      - map listener thread to event arg storage
 *  - calling signal again before child is waiting does not do anything, child must wait till next cycle
 *      - good solution, alternative is semaphore which would let child run at max speed until caught up with parent
 * - while not interrupted, keep calling env's listener with shared memory
 *
 * runner threads need a unique identifier from AtomicLong
 *  - created runner thread has unique ID in Task map
 *      - in lifetime of thread, listeners are registered under unique parent ID
 *          - listener threads are created with uniqueParentID, scriptEnvironment, eventType
 *          - listeners stored in listener list under unique ID
 *  - when runner dies, Task map has runner task removed
 *  - during runner lifetime or after, either runner kills child itself or child kills itself
 * */