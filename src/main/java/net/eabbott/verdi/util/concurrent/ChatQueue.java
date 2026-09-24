package net.eabbott.verdi.util.concurrent;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.network.chat.Component;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.ReentrantLock;

public class ChatQueue {
    private final Queue<String> queue = new LinkedList<>();
    private final ReentrantLock lock = new ReentrantLock();
    private Minecraft minecraft = null;
    private ChatComponent chat = null;

    public void push(String text) {
        lock.lock();
        try {
            queue.add(text);
        } finally {
            lock.unlock();
        }
    }

    public void flush() {
        lock.lock();
        try {
            if (minecraft == null || chat == null) {
                minecraft = Minecraft.getInstance();
                chat = minecraft.gui.hud.getChat();
            }
            while (!queue.isEmpty()) {
                chat.addClientSystemMessage(Component.nullToEmpty(queue.poll()));
            }
        } finally {
            lock.unlock();
        }
    }
}
