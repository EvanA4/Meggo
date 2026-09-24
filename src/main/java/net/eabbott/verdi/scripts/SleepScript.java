package net.eabbott.verdi.scripts;

import net.eabbott.verdi.VerdiScript;
import net.eabbott.verdi.util.VerdiChat;

public class SleepScript extends VerdiScript {
    int ticksToSleep = -1;

    @Override
    public boolean isMotor() {
        return false;
    }

    @Override
    public void run(String[] args) {
        if (args.length != 2) {
            VerdiChat.send("usage: sleep <number of ticks>");
            exit();
        }

        try {
            this.ticksToSleep = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            VerdiChat.send("usage: sleep <number of ticks>");
            exit();
        }
    }

    @Override
    public void onClientWorldTick() {
        if (ticksToSleep == 0) {
            VerdiChat.send("Finished sleeping!");
            exit();
        }
        else if (ticksToSleep != -1) --ticksToSleep;
    }
}
