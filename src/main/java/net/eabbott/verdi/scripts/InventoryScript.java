package net.eabbott.verdi.scripts;

import net.eabbott.verdi.MeggoScript;
import net.eabbott.verdi.dataclasses.LevelRenderContext;
import net.eabbott.verdi.util.input.PlayerKey;
import net.eabbott.verdi.util.input.PlayerMover;
import net.minecraft.world.inventory.ContainerInput;

public class InventoryScript extends MeggoScript {
    public InventoryScript(Long runnerID, String name) {
        super(runnerID, name);
    }

    @Override
    public void run(String[] args) {
        try {
            PlayerMover.clickBinding(PlayerKey.INVENTORY);
            Thread.sleep(500);
            PlayerMover.changeInventory(36, 0, ContainerInput.PICKUP);
            PlayerMover.changeInventory(37, 0, ContainerInput.PICKUP);
            Thread.sleep(500);
            PlayerMover.closeInventory();
        } catch (Exception _) {}
    }

    @Override
    public void render(LevelRenderContext levelRenderContext) {

    }

    @Override
    public boolean isMotor() {
        return false;
    }
}
