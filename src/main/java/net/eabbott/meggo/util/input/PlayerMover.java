package net.eabbott.meggo.util.input;

import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.Vec2;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;

public class PlayerMover {
    private final HashMap<MeggoInput, Boolean> forced = new HashMap<>();

    public PlayerMover() {
        for (MeggoInput input : MeggoInput.values()) {
            forced.put(input, false);
        }
    }

    public boolean getInput(MeggoInput input) {
        return forced.get(input);
    }

    public void setInput(MeggoInput input, boolean enabled) {
        forced.put(input, enabled);
    }

    public void setView(float pitch, float yaw) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null) {
            mc.player.setXRot(pitch);
            mc.player.setYRot(yaw);
        }
    }

    public void setView(Vec2 view) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null) {
            mc.player.setXRot(view.x);
            mc.player.setYRot(view.y);
        }
    }

    public @Nullable Vec2 getView() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null) {
            return mc.player.getRotationVector();
        }
        return null;
    }
}
