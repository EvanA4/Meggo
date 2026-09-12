package net.eabbott.meggo.scripts;

import net.eabbott.meggo.MeggoEvent;
import net.eabbott.meggo.MeggoManager;
import net.eabbott.meggo.MeggoScript;

public class SleepScript extends MeggoScript {
    public SleepScript(Long runnerID, String name) {
        super(runnerID, name);
    }

    @Override
    public void run(String[] args) {
        if (args.length != 2) {
            MeggoManager.print("Usage: \\sleep <seconds>");
            return;
        }

        try {
            addListener(MeggoEvent.MOUSE_CLICK);
            Thread.sleep(Long.parseLong(args[1]) * 1000L);
            removeListener(MeggoEvent.MOUSE_CLICK);
            MeggoManager.print(String.format("Slept %s seconds.", args[1]));
        } catch (Exception e) {
            MeggoManager.print("Usage: \\sleep <seconds>");
        }
    }

    @Override
    public void onMouseClick(int button, int action, int modifiers, double xpos, double ypos) {
        if (button == 0 && action == 1) {
            MeggoManager.print("Registered left mouse click!");
        }
    }
}
