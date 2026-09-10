package net.eabbott.meggo.scripts;

import net.eabbott.meggo.MeggoManager;
import net.eabbott.meggo.MeggoScript;

public class SleepScript extends MeggoScript {
    @Override
    public String getName() {
        return "sleep";
    }

    @Override
    public void run(String[] args) {
        if (args.length != 2) {
            MeggoManager.print("Usage: \\sleep <seconds>");
            return;
        }

        try {
            Thread.sleep(Long.parseLong(args[1]) * 1000L);
            MeggoManager.print(String.format("Slept %s seconds.", args[1]));
        } catch (Exception e) {
            MeggoManager.print("Usage: \\sleep <seconds>");
        }
    }
}
