package net.eabbott.meggo.util;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.CollisionGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.pathfinder.PathTypeCache;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import org.jspecify.annotations.Nullable;

public class PlayerPathfindingContext {
    private final CollisionGetter level;
    private final @Nullable PathTypeCache cache;
    private final BlockPos playerPosition;
    private final BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

    public PlayerPathfindingContext(final CollisionGetter level, final LocalPlayer player) {
        this.level = level;
        if (player.level() instanceof ServerLevel serverLevel) {
            this.cache = serverLevel.getPathTypeCache();
        } else {
            this.cache = null;
        }

        this.playerPosition = player.blockPosition();
    }

    public PathType getPathTypeFromState(final int x, final int y, final int z) {
        BlockPos pos = this.mutablePos.set(x, y, z);
        return this.cache == null ? PlayerNodeEvaluator.getPathTypeFromState(this.level, pos) : this.cache.getOrCompute(this.level, pos);
    }

    public BlockState getBlockState(final BlockPos pos) {
        return this.level.getBlockState(pos);
    }

    public CollisionGetter level() {
        return this.level;
    }

    public BlockPos mobPosition() {
        return this.playerPosition;
    }
}
