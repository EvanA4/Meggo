package net.eabbott.verdi.behavior.legal;

import net.eabbott.verdi.behavior.AbstractBehavior;
import net.eabbott.verdi.behavior.PlayerBrain;
import net.eabbott.verdi.util.VerdiChat;
import net.eabbott.verdi.util.input.BlockView;
import net.eabbott.verdi.util.input.PlayerKey;
import net.eabbott.verdi.util.input.PlayerMover;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec2;
import org.jspecify.annotations.Nullable;

import java.util.function.Consumer;

public class BreakBlockBehavior extends AbstractBehavior {
    private final ClientLevel level;
    @Nullable Consumer<Boolean> resolve = null;
    boolean success = false;
    BlockPos toBreak;
    BlockState originalState;
    @Nullable Vec2 breakView = null;
    boolean isBreaking = false;

    public BreakBlockBehavior(ClientLevel level, BlockPos toBreak, PlayerBrain brain, @Nullable Consumer<Boolean> resolve) {
        super(brain);
        this.resolve = resolve;
        this.toBreak = toBreak;
        this.level = level;
        this.originalState = level.getBlockState(toBreak);
    }

    @Override
    public void tick() {
        // If not already, look at block
        if (breakView == null) {
            this.breakView = BlockView.getView(toBreak);
            if (this.breakView != null) {
                VerdiChat.send(
                    "Breaking block at (%d, %d, %d)...",
                    this.toBreak.getX(), this.toBreak.getY(), this.toBreak.getZ()
                );
                PlayerMover.setView(this.breakView);
            }
            else {
                VerdiChat.send("Unable to reach block from current position.");
                this.finish();
                return;
            }
        }

        BlockState newState = this.level.getBlockState(this.toBreak);

        // if block changed, quit
        if (!newState.getBlock().equals(originalState.getBlock())) {
            PlayerMover.setBinding(PlayerKey.ATTACK, false);
            this.finish();
            return;
        }

        // if not, start breaking if not already
        if (!this.isBreaking) PlayerMover.setBinding(PlayerKey.ATTACK, true);
    }

    @Override
    public void finish() {
        if (this.resolve != null) this.resolve.accept(this.success);
        this.brain.removeBehavior();
    }
}
