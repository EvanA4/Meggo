package net.eabbott.verdi.util.input;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.phys.Vec2;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;

public class PlayerMover {
    private static final HashMap<String, InputConstants.Key> bindings = new HashMap<>();
    private static int currentContainerID = -1;

    public static void setView(float pitch, float yaw) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null) {
            mc.player.setXRot(pitch);
            mc.player.setYRot(yaw);
        }
    }

    public static void setView(Vec2 view) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null) {
            mc.player.setXRot(view.x);
            mc.player.setYRot(view.y);
        }
    }

    public static @Nullable Vec2 getView() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null) {
            return mc.player.getRotationVector();
        }
        return null;
    }

    public static void setBinding(String name, InputConstants.Key key) {
        bindings.put(name, key);
    }

    public static InputConstants.Key getBinding(String name) {
        return bindings.get(name);
    }

    public static void clickBinding(PlayerKey name) {
        KeyMapping.click(PlayerMover.getBinding(name.getValue()));
    }

    public static void setBinding(PlayerKey name, boolean enabled) {
        KeyMapping.set(PlayerMover.getBinding(name.getValue()), enabled);
    }

    public static void setCurrentContainer(int newContainerID) {
        currentContainerID = newContainerID;
    }

    public static void changeInventory(final int slotNum, final int buttonNum, final ContainerInput containerInput) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.gameMode == null) return;
        mc.gameMode.handleContainerInput(currentContainerID, slotNum, buttonNum, containerInput, mc.player);
    }

    public static void closeInventory() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.gui.screen() == null || !mc.gui.screen().shouldCloseOnEsc()) return;
        mc.gui.screen().onClose();
    }
}