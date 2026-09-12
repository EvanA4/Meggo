package net.eabbott.meggo.scripts;

import net.eabbott.meggo.MeggoManager;
import net.eabbott.meggo.MeggoScript;
import net.eabbott.meggo.dataclasses.LevelRenderContext;
import net.eabbott.meggo.util.MeggoUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.gizmos.GizmoStyle;
import net.minecraft.gizmos.Gizmos;
import net.minecraft.util.ARGB;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.AABB;
import org.jspecify.annotations.Nullable;

public class FindPathScript extends MeggoScript {
    private static @Nullable Path PATH_TO_RENDER = null;

    public FindPathScript(Long runnerID, String name) {
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
        PATH_TO_RENDER = MeggoUtil.getPath(toPos);

        try {
          Thread.sleep(5000); // sleep 5 seconds so player can view path
        } catch (InterruptedException _) {}
    }

    @Override
    public void render(LevelRenderContext levelRenderContext) {
        var color       = ARGB.color(0, 0, 200, 255);
        var fill_color  = ARGB.color(50,  0, 200, 255);
        var filled      = GizmoStyle.strokeAndFill(color, 1.5f, fill_color);

        if (PATH_TO_RENDER != null) {
            for (int i = 0; i < PATH_TO_RENDER.getNodeCount(); ++i) {
                Node node = PATH_TO_RENDER.getNode(i);
                var box = new AABB(new BlockPos(node.x, node.y, node.z));
                Gizmos.cuboid(box, filled).setAlwaysOnTop();
            }
        }
    }

    @Override
    public boolean isMotor() {
        return false;
    }
}
