package net.eabbott.verdi.util.math;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

public class VerdiMath {
    public static Vec2 getAngle(Vec3 to, Vec3 from) {
        double dx = to.x - from.x;
        double dy = to.y - from.y;
        double dz = to.z - from.z;
        return new Vec3(dx, dy, dz).rotation();
    }

    public static ViewBounds getViewBounds(VoxelShape voxelShape, BlockPos blockPos, Vec3 cameraPos) {
        AABB bounds = voxelShape.bounds();
        double[] xs = {bounds.minX + blockPos.getX(), bounds.maxX + blockPos.getX()};
        double[] ys = {bounds.minY + blockPos.getY(), bounds.maxY + blockPos.getY()};
        double[] zs = {bounds.minZ + blockPos.getZ(), bounds.maxZ + blockPos.getZ()};

        double minYaw = Double.POSITIVE_INFINITY;
        double minPitch = Double.POSITIVE_INFINITY;
        double maxYaw = Double.NEGATIVE_INFINITY;
        double maxPitch = Double.NEGATIVE_INFINITY;

        for (double x : xs) {
            for (double y : ys) {
                for (double z : zs) {
                    Vec3 corner = new Vec3(x, y, z);
                    Vec2 view = VerdiMath.getAngle(corner, cameraPos);
                    if (view.x < minPitch) minPitch = view.x;
                    if (view.x > maxPitch) maxPitch = view.x;
                    if (view.y < minYaw) minYaw = view.y;
                    if (view.y > maxYaw) maxYaw = view.y;
                }
            }
        }

        return new ViewBounds(minPitch, minYaw, maxPitch, maxYaw);
    }

    public static Vec3 rotationToDirection(Vec2 rotation) {
        float pitch = rotation.x;
        float yaw = rotation.y;

        float pitchRadians = pitch * ((float) Math.PI / 180.0F);
        float yawRadians = yaw * ((float) Math.PI / 180.0F);

        float cosPitch = Mth.cos(pitchRadians);

        return new Vec3(
            -Mth.sin(yawRadians) * cosPitch,
            -Mth.sin(pitchRadians),
            Mth.cos(yawRadians) * cosPitch
        );
    }
}