package net.eabbott.verdi.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.network.chat.Component;

public class VerdiChat {
    private static Minecraft minecraft = null;

    public static void send(String message) {
        if (minecraft == null) minecraft = Minecraft.getInstance();
        ChatComponent chat = minecraft.gui.hud.getChat();
        chat.addClientSystemMessage(Component.nullToEmpty(message));
    }

    public static void send(String format, Object... args) {
        send(String.format(format, args));
    }
}
