package net.eabbott.verdi.util;

import net.eabbott.verdi.util.path.PlayerNodeEvaluator;
import net.eabbott.verdi.util.path.PlayerPathFinder;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.PathNavigationRegion;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jspecify.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public class VerdiUtil {
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

    public static @Nullable Path getPath(BlockPos target) {
        int maxPathLength = 128;
        int radiusOffset = 0;
        int reachRange = 1;
        float maxVisitedNodesMultiplier = 1;

        int radius = (int)(maxPathLength + radiusOffset);
        var minecraft = Minecraft.getInstance();
        var world = minecraft.level;
        var player = minecraft.player;
        if (world == null || player == null) return null;
        BlockPos fromPos = player.blockPosition();
        PlayerPathFinder ppf = new PlayerPathFinder(
                new PlayerNodeEvaluator(),
                128
        );
        Set<BlockPos> targets = new HashSet<BlockPos>();
        targets.add(target);

        PathNavigationRegion region = new PathNavigationRegion(world, fromPos.offset(-radius, -radius, -radius), fromPos.offset(radius, radius, radius));
        return ppf.findPath(region, player, targets, maxPathLength, reachRange, maxVisitedNodesMultiplier);
    }
}
