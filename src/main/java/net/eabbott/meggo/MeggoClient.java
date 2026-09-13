package net.eabbott.meggo;

import net.eabbott.meggo.scripts.*;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientChunkEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenKeyboardEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;

public class MeggoClient implements ClientModInitializer  {
    @Override
    public void onInitializeClient() {
        ClientChunkEvents.CHUNK_LOAD.register(MeggoManager::onChunkLoad);
        ClientChunkEvents.CHUNK_UNLOAD.register(MeggoManager::onChunkUnload);

        MeggoManager.init();
        MeggoManager.addScript("args", ArgsScript.class);
        MeggoManager.addScript("sleep", SleepScript.class);
        MeggoManager.addScript("listen", ListenScript.class);
        MeggoManager.addScript("zombie", ZombieScript.class);
        MeggoManager.addScript("motor", MotorScript.class);
        MeggoManager.addScript("findPath", FindPathScript.class);
        MeggoManager.addScript("move", MoveScript.class);

        ClientTickEvents.START_LEVEL_TICK.register(world -> MeggoManager.onClientWorldTick());
        ScreenEvents.AFTER_INIT.register(this::afterInitScreen);
    }

    private void afterInitScreen(Minecraft client, Screen screen, int windowWidth, int windowHeight) {
        if (screen instanceof ChatScreen) {
            ScreenKeyboardEvents.allowKeyPress(screen)
                    .register((_screen, event) -> !MeggoManager.onKeyboardKeyPressed(_screen, event.key()));
        }
    }
}
