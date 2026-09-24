package net.eabbott.verdi;

import net.eabbott.verdi.scripts.*;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientChunkEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenKeyboardEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;

public class VerdiClient implements ClientModInitializer  {
    @Override
    public void onInitializeClient() {
        ClientChunkEvents.CHUNK_LOAD.register(VerdiShell::onChunkLoad);
        ClientChunkEvents.CHUNK_UNLOAD.register(VerdiShell::onChunkUnload);

        // VerdiShell.addScript("path", PathScript.class);
        VerdiShell.addScript("find", FindScript.class);
        // VerdiShell.addScript("craft", CraftScript.class);

        ClientTickEvents.START_LEVEL_TICK.register(_ -> VerdiShell.onClientWorldTick());
    }
}
