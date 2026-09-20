package net.eabbott.meggo.scripts;

import net.eabbott.meggo.MeggoEvent;
import net.eabbott.meggo.MeggoManager;
import net.eabbott.meggo.MeggoScript;
import net.eabbott.meggo.dataclasses.LevelRenderContext;
import net.eabbott.meggo.util.MeggoMath;
import net.eabbott.meggo.util.MeggoUtil;
import net.eabbott.meggo.util.input.PlayerKey;
import net.eabbott.meggo.util.input.PlayerMover;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.gizmos.GizmoStyle;
import net.minecraft.gizmos.Gizmos;
import net.minecraft.util.ARGB;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class PathScript extends MeggoScript {
    private @Nullable Path path = null;

    public PathScript(Long runnerID, String name) {
        super(runnerID, name);
    }

    @Override
    public void run(String[] args) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null) return;

        BlockPos fromPos = minecraft.player.blockPosition();
        BlockPos toPos = MeggoUtil.getTargetedBlock(64);
        if (toPos == null) return;

        MeggoManager.print(String.format(
                "Computing path from (%d, %d, %d) to (%d, %d, %d)",
                fromPos.getX(),
                fromPos.getY(),
                fromPos.getZ(),
                toPos.getX(),
                toPos.getY(),
                toPos.getZ()
        ));
        path = MeggoUtil.getPath(toPos);

        addListener(MeggoEvent.RENDER_BEGIN);
    }

    @Override
    public void render(LevelRenderContext levelRenderContext) {
        var color       = ARGB.color(0, 0, 200, 255);
        var fill_color  = ARGB.color(50,  0, 200, 255);
        var filled      = GizmoStyle.strokeAndFill(color, 1.5f, fill_color);

        if (path != null) {
            for (int i = 0; i < path.getNodeCount(); ++i) {
                Node node = path.getNode(i);
                var box = new AABB(new BlockPos(node.x, node.y, node.z));
                Gizmos.cuboid(box, filled).setAlwaysOnTop();
            }
        }
    }

    @Override
    public void onRenderBegin(LevelRenderContext levelRenderContext) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null || path == null) {
            removeListener(MeggoEvent.RENDER_BEGIN);
            return;
        }

        Node next = path.getNextNode();
        BlockPos nextBP = new BlockPos(next.x, next.y, next.z);
        Vec3 nextV3 = new Vec3(next.x + .5, next.y, next.z + .5);
        if (player.blockPosition().equals(nextBP)) {
            path.advance();
        }

        if (!Thread.interrupted() && !path.isDone()) {
            Vec2 forward = MeggoMath.getAngle(nextV3, player.position());
            PlayerMover.setView(forward);
            PlayerMover.setBinding(PlayerKey.JUMP, player.position().y < next.y);
            PlayerMover.setBinding(PlayerKey.FORWARD, true);

        } else {
            PlayerMover.setBinding(PlayerKey.FORWARD, false);
            PlayerMover.setBinding(PlayerKey.JUMP, false);
            removeListener(MeggoEvent.RENDER_BEGIN);
        }
    }

    @Override
    public boolean isMotor() {
        return true;
    }
}
