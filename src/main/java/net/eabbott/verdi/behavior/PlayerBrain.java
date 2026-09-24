package net.eabbott.verdi.behavior;

import net.eabbott.verdi.behavior.result.AbstractBehaviorResult;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class PlayerBrain {
    public final List<AbstractBehaviorResult> results = new ArrayList<>();
    public final Deque<PlayerBehavior> behaviors = new ArrayDeque<>();
}
