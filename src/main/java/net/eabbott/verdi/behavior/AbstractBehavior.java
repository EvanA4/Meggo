package net.eabbott.verdi.behavior;

public abstract class AbstractBehavior {
    protected PlayerBrain brain;

    public AbstractBehavior(PlayerBrain brain) {
        this.brain = brain;
    }

    // public PlayerBehavior(PlayerBrain brain, Consumer<BehaviorResult> callback) {}
    public abstract void tick();
    public abstract void finish();
}
