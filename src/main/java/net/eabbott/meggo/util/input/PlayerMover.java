package net.eabbott.meggo.util.input;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.Vec2;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;

public class PlayerMover {
    private static final HashMap<String, InputConstants.Key> bindings = new HashMap<>();

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
}
