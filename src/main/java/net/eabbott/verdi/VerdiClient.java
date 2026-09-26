package net.eabbott.verdi;

import net.eabbott.verdi.scripts.*;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientChunkEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class VerdiClient implements ClientModInitializer  {
    @Override
    public void onInitializeClient() {
        ClientChunkEvents.CHUNK_LOAD.register(VerdiShell::onChunkLoad);
        ClientChunkEvents.CHUNK_UNLOAD.register(VerdiShell::onChunkUnload);

        // VerdiShell.addScript("path", PathScript.class);
        VerdiShell.addScript("find", FindScript.class);
        VerdiShell.addScript("sleep", SleepScript.class);
        VerdiShell.addScript("break_block", BreakBlockScript.class);
        // VerdiShell.addScript("craft", CraftScript.class);

        ClientTickEvents.START_LEVEL_TICK.register(_ -> VerdiShell.onClientWorldTick());
    }
}
