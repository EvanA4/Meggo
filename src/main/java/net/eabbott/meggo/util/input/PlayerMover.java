package net.eabbott.meggo.util.input;

import java.util.HashMap;

public class PlayerMover {
    private final HashMap<MeggoInput, Boolean> forced = new HashMap<>();

    public PlayerMover() {
        for (MeggoInput input : MeggoInput.values()) {
            forced.put(input, false);
        }
    }

    public boolean getInput(MeggoInput input) {
        return forced.get(input);
    }

    public void setInput(MeggoInput input, boolean enabled) {
        forced.put(input, enabled);
    }
}
