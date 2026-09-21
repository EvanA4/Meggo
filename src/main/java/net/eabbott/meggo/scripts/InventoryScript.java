package net.eabbott.meggo.scripts;

import net.eabbott.meggo.MeggoScript;
import net.eabbott.meggo.dataclasses.LevelRenderContext;
import net.eabbott.meggo.util.input.PlayerKey;
import net.eabbott.meggo.util.input.PlayerMover;
import net.minecraft.world.inventory.ContainerInput;

public class InventoryScript extends MeggoScript {
    public InventoryScript(Long runnerID, String name) {
        super(runnerID, name);
    }

    @Override
    public void run(String[] args) {
//        PlayerMover.clickBinding(PlayerKey.INVENTORY);
//        PlayerMover.changeInventory(36, 0, ContainerInput.PICKUP);
//        PlayerMover.changeInventory(37, 0, ContainerInput.PICKUP);
        PlayerMover.clickBinding(PlayerKey.TOGGLE_GUI);
    }

    @Override
    public void render(LevelRenderContext levelRenderContext) {

    }

    @Override
    public boolean isMotor() {
        return false;
    }
}
