package net.eabbott.verdi.behavior;

import net.eabbott.verdi.VerdiScript;

import java.util.Stack;

public class PlayerBrain {
    private final Stack<AbstractBehavior> behaviors = new Stack<>();
    private boolean shouldExit = false;
    private final VerdiScript script;

    public PlayerBrain(VerdiScript script) {
        this.script = script;
    }

    public void addRootBehavior(AbstractBehavior behavior) {
        this.behaviors.push(behavior);
    }

    public void tick() {
        if (this.behaviors.isEmpty()) this.script.exit();
        else this.behaviors.peek().tick();
        if (this.shouldExit) this.script.exit();
    }

    public void requestExit() {
        this.shouldExit = true;
    }

    public void addBehavior(AbstractBehavior behavior) {
        this.behaviors.push(behavior);
    }

    public void removeBehavior() {
        this.behaviors.pop();
    }
}
