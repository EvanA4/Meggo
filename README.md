# Meggo

## Philosophy

There will be four layers to the mod:
1. The **shell environment** will only have one task running at a time. The task will run on the main thread. Because of this, tasks are encouraged to put any intense computation on a separate thread. Tasks can have event listeners, and they will always be called. A task only truly dies when it interacts with the **shell API**.
   1. At the same level is the **player API**. This API offers simple player control primitives for inventory management, movement, etc.
   2. At the same level is the **world API**. This API offers block accessing, block searching, and entity searching.
2. The **task environment** defines all of a task's event listeners and main function. The main function is called only upon startup.
3. A **behavior manager** tracks the current behavior the player brain should follow, using a deque to store behaviors.
4. A **behavior** always has a tick function for the manager to call, but only the behavior at the front of the deque is ticked. Behaviors have a few options through the behavior manager:
    - They can _finish_, removing themselves from the deque. They must provide a result to the manager for future behaviors to interact with. The manager stores these results as a list.
    - They can _add_ behaviors to the front of the deque.
    - They can _error_, telling the manager to quit.

## Setup

For setup instructions, please see the [Fabric Documentation page](https://docs.fabricmc.net/develop/getting-started/creating-a-project#setting-up) related to the IDE that you are using.

### License

This template is available under the CC0 license. Feel free to learn from it and incorporate it in your own projects.

### Credit

Much of the boilerplate for this mod was pulled directly from [Minescript](https://github.com/maxuser0/minescript/tree/main).