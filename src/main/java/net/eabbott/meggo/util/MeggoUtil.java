package net.eabbott.meggo.util;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jspecify.annotations.Nullable;

public class MeggoUtil {
    public static @Nullable BlockPos getTargetedBlock(double maxDistance) {
        var minecraft = Minecraft.getInstance();
        var entity = minecraft.getCameraEntity();
        if (entity == null) return null;

        var blockHit = entity.pick(maxDistance, 0.0f, false);
        if (blockHit.getType() != HitResult.Type.BLOCK) return null;
        var hitResult = (BlockHitResult) blockHit;
        return hitResult.getBlockPos();
    }

    public static @Nullable Iterable<Entity> getEntities() {
        var minecraft = Minecraft.getInstance();
        var world = minecraft.level;
        if (world == null) return null;
        return world.entitiesForRendering();
    }
}
