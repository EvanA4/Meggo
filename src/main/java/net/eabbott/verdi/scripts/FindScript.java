package net.eabbott.verdi.scripts;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.eabbott.verdi.VerdiScript;
import net.eabbott.verdi.dataclasses.LevelRenderContext;
import net.eabbott.verdi.util.VerdiChat;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.blocks.BlockStateParser;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.registries.VanillaRegistries;
import net.minecraft.gizmos.GizmoStyle;
import net.minecraft.gizmos.Gizmos;
import net.minecraft.util.ARGB;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.AABB;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FindScript extends VerdiScript {
    ExecutorService executor = Executors.newSingleThreadExecutor();
    Minecraft mc = Minecraft.getInstance();
    @Nullable Future<List<BlockPos>> found = null;
    boolean havePrintedResult = false;
    int ticksToSleep = 200; // 10 seconds

    @Override
    public void run(String[] args) {
        if (args.length != 3) {
            VerdiChat.send("usage: find <block type> <range>");
            return;
        }

        CommandBuildContext context = Commands.createValidationContext(VanillaRegistries.createLookup());
        HolderLookup.RegistryLookup<Block> blocks = context.lookupOrThrow(Registries.BLOCK);
        try {
            BlockStateParser.BlockResult result = BlockStateParser.parseForBlock(blocks, args[1], true);
            Block target = result.blockState().getBlock();
            this.found = asyncFindBlock(target, Integer.parseInt(args[2]));

        } catch (CommandSyntaxException e) {
            VerdiChat.send("Error: invalid block type.");
            exit();
        } catch (NumberFormatException e) {
            VerdiChat.send("Error: invalid range.");
            exit();
        }
    }

    public Future<List<BlockPos>> asyncFindBlock(Block target, int searchRange) {
        return executor.submit(() -> findBlock(target, searchRange));
    }

    private List<BlockPos> findBlock(Block target, int searchRange) {
        if (this.mc.player == null || this.mc.level == null) return new Vector<>();
        BlockPos startPos = this.mc.player.blockPosition();
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        Vector<BlockPos> output = new Vector<>();

        if (this.mc.level.getBlockState(startPos).is(target)) {
            output.add(startPos);
        }

        // search in increasing range of cube shells
        for (int offset = 1; offset <= searchRange; ++offset) {
            // x faces (entire face)
            for (int z = -offset; z <= offset; ++z) {
                for (int y = -offset; y <= offset; ++y) {
                    pos.setWithOffset(startPos, offset, y, z);
                    if (this.mc.level.getBlockState(pos).is(target)) output.add(new BlockPos(pos));
                    pos.setWithOffset(startPos, -offset, y, z);
                    if (this.mc.level.getBlockState(pos).is(target)) output.add(new BlockPos(pos));
                }
            }

            // z faces (inner square + vertical edges - corners)
            for (int y = -offset; y <= offset; ++y) {
                for (int x = -offset + 1; x <= offset - 1; ++x) {
                    pos.setWithOffset(startPos, x, y, offset);
                    if (this.mc.level.getBlockState(pos).is(target)) output.add(new BlockPos(pos));
                    pos.setWithOffset(startPos, x, y, -offset);
                    if (this.mc.level.getBlockState(pos).is(target)) output.add(new BlockPos(pos));
                }
            }

            // y faces (just inner square)
            for (int z = -offset + 1; z <= offset - 1; ++z) {
                for (int x = -offset + 1; x <= offset - 1; ++x) {
                    pos.setWithOffset(startPos, x, offset, z);
                    if (this.mc.level.getBlockState(pos).is(target)) output.add(new BlockPos(pos));
                    pos.setWithOffset(startPos, x, -offset, z);
                    if (this.mc.level.getBlockState(pos).is(target)) output.add(new BlockPos(pos));
                }
            }
        }

        return output;
    }

    @Override
    public void onRenderBegin(LevelRenderContext levelRenderContext) {
        if (this.found != null && this.found.isDone()) {
            try {
                List<BlockPos> toRender = this.found.get();

                if (!this.havePrintedResult) {
                    if (!toRender.isEmpty()) {
                        VerdiChat.send("Found %d block(s).", toRender.size());
                        Thread.sleep(60 * 1000);
                    } else {
                        VerdiChat.send("Could not find block within specified range.");
                        exit();
                    }
                    this.havePrintedResult = true;
                }

                var color       = ARGB.color(0, 0, 200, 255);
                var fill_color  = ARGB.color(50,  0, 200, 255);
                var filled      = GizmoStyle.strokeAndFill(color, 1.5f, fill_color);
                for (BlockPos pos : toRender) {
                    var box = new AABB(new BlockPos(pos.getX(), pos.getY(), pos.getZ()));
                    Gizmos.cuboid(box, filled).setAlwaysOnTop();
                }

            } catch (Exception e) {
                VerdiChat.send("Failed to access list of found blocks: %s", e.getMessage());
                exit();
            }
        }
    }

    @Override
    public void onClientWorldTick() {
        if (havePrintedResult) {
            if (ticksToSleep > 0) --ticksToSleep;
            else exit();
        }
    }

    @Override
    public boolean isMotor() {
        return false;
    }
}
