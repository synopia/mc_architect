package de.funky_clan.mc.eventbus;

/**
 * @author synopia
 */
public final class VetoEvent implements Event {
    private final Event       event;
    private final VetoHandler handler;

    public VetoEvent( Event event, VetoHandler handler ) {
        this.event   = event;
        this.handler = handler;
    }

    public final Event getEvent() {
        return event;
    }

    public VetoHandler getHandler() {
        return handler;
    }
}
