package net.eabbott.meggo.util.input;

import net.minecraft.client.player.ClientInput;
import net.minecraft.world.phys.Vec2;

public class ForcedClientInput extends ClientInput {
    PlayerMover playerMover;

    public ForcedClientInput(PlayerMover playerMover) {
        this.playerMover = playerMover;
    }

    @Override
    public void tick() {
        float leftImpulse = 0.0F;
        float forwardImpulse = 0.0F;
        boolean jumping = playerMover.getInput(MeggoInput.JUMP); // oppa gangnam style

        boolean up = playerMover.getInput(MeggoInput.MOVE_FORWARD);
        if (up) {
            forwardImpulse++;
        }

        boolean down = playerMover.getInput(MeggoInput.MOVE_BACK);
        if (down) {
            forwardImpulse--;
        }

        boolean left = playerMover.getInput(MeggoInput.MOVE_LEFT);
        if (left) {
            leftImpulse++;
        }

        boolean right = playerMover.getInput(MeggoInput.MOVE_RIGHT);
        if (right) {
            leftImpulse--;
        }

        boolean sneaking = playerMover.getInput(MeggoInput.SNEAK);
        if (sneaking) {
            leftImpulse *= 0.3F;
            forwardImpulse *= 0.3F;
        }

        boolean sprinting = playerMover.getInput(MeggoInput.SPRINT);

        this.keyPresses = new net.minecraft.world.entity.player.Input(up, down, left, right, jumping, sneaking, sprinting);
        this.moveVector = new Vec2(leftImpulse, forwardImpulse);
    }
}
