package de.funky_clan.mc.eventbus;

/**
 * @author synopia
 */
public class EventDispatcher {
    private static EventDispatcher INSTANCE = new EventDispatcher();
    private EventBus modelEventBus;

    private EventDispatcher() {
        modelEventBus = new EventBus();
    }

    public static EventDispatcher getDispatcher() {
        return INSTANCE;
    }

    public EventBus getModelEventBus() {
        return modelEventBus;
    }
}
