package net.eabbott.verdi.util.input;

import net.eabbott.verdi.util.math.VerdiMath;
import net.eabbott.verdi.util.math.ViewBounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.*;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

public class BlockView {
    private static final int NUM_STEPS = 10;
    private static final double EPSILON = .001;

    public static @Nullable Vec2 getView(BlockPos blockPos) {
        if (Minecraft.getInstance().player == null || Minecraft.getInstance().level == null) return null;
        LocalPlayer player = Minecraft.getInstance().player;
        Vec3 cameraPos = player.getEyePosition();
        ClientLevel level = Minecraft.getInstance().level;

        BlockState blockState = level.getBlockState(blockPos);
        if (blockState.isAir()) return null;

        VoxelShape voxelShape = blockState.getShape(level, blockPos);


        ViewBounds viewBounds = VerdiMath.getViewBounds(voxelShape, blockPos, cameraPos);

        boolean flipPitchDir = viewBounds.maxPitch - viewBounds.minPitch > Mth.HALF_PI;
        boolean flipYawDir = viewBounds.maxYaw - viewBounds.minYaw > Mth.HALF_PI;
        double pitchDiff = Math.min(
            viewBounds.maxPitch - viewBounds.minPitch,
            Mth.HALF_PI - viewBounds.maxPitch + viewBounds.minPitch
        );
        double yawDiff = Math.min(
                viewBounds.maxYaw - viewBounds.minYaw,
                Mth.HALF_PI - viewBounds.maxYaw + viewBounds.minYaw
        );
        double pitchStep = pitchDiff / NUM_STEPS * (flipPitchDir ? -1 : 1);
        double yawStep = yawDiff / NUM_STEPS * (flipYawDir ? -1 : 1);

        for (double pitch = viewBounds.minPitch; pitch < viewBounds.maxPitch + EPSILON; pitch += pitchStep) {
            for (double yaw = viewBounds.minYaw; yaw < viewBounds.maxYaw + EPSILON; yaw += yawStep) {
                Vec2 view = new Vec2((float) pitch, Mth.wrapDegrees((float) yaw));
                Vec3 viewDir = VerdiMath.rotationToDirection(view);
                if (hitsBlock(player, level, cameraPos, viewDir, blockPos)) {
                    return view;
                }
            }
        }

        return null;
    }

    private static boolean hitsBlock(
        LocalPlayer player,
        ClientLevel level,
        Vec3 cameraPos,
        Vec3 cameraDir,
        BlockPos blockPos
    ) {
        Vec3 end = cameraPos.add(cameraDir.scale(
            player.getAttributeValue(Attributes.BLOCK_INTERACTION_RANGE)
        ));

        BlockHitResult result = level.clip(new ClipContext(
                cameraPos,
                end,
                ClipContext.Block.OUTLINE,
                ClipContext.Fluid.NONE,
                player
        ));

        if (result.getType() == HitResult.Type.BLOCK) {
            return result.getBlockPos().equals(blockPos);
        }
        return false;
    }
}
