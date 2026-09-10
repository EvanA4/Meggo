package net.eabbott.meggo.scripts;

import net.eabbott.meggo.MeggoManager;
import net.eabbott.meggo.MeggoScript;

public class ArgsScript extends MeggoScript {
    @Override
    public String getName() {
        return "example";
    }

    @Override
    public void run(String[] args) {
        MeggoManager.print("Example script received arguments:");
        for (int i = 0; i < args.length; ++i) {
            MeggoManager.print(String.format("    [%d]: \"%s\"", i, args[i]));
        }
    }
}
