package net.eabbott.verdi.scripts;

import net.eabbott.verdi.VerdiScript;
import net.eabbott.verdi.behavior.PlayerBrain;
import net.eabbott.verdi.behavior.legal.BreakBlockBehavior;
import net.eabbott.verdi.util.VerdiChat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;

public class BreakBlockScript extends VerdiScript {
    PlayerBrain brain;

    @Override
    public boolean isMotor() {
        return true;
    }

    @Override
    public void run(String[] args) {
        ClientLevel level = Minecraft.getInstance().level;
        if (args.length != 4 || level == null) {
            VerdiChat.send("usage: break_block <x> <y> <z>");
            exit();
            return;
        }

        try {
            int x = Integer.parseInt(args[1]);
            int y = Integer.parseInt(args[2]);
            int z = Integer.parseInt(args[3]);
            BlockPos toBreak = new BlockPos(x, y, z);

            this.brain = new PlayerBrain(this);
            this.brain.addRootBehavior(new BreakBlockBehavior(level, toBreak, this.brain, null));

        } catch (Exception e) {
            VerdiChat.send("usage: break_block <x> <y> <z>");
        }
    }

    @Override
    public void onClientWorldTick() {
        this.brain.tick();
    }
}
