package de.funky_clan.mc.eventbus;

/**
 * @author synopia
 */
public final class VetoEvent implements Event {
    private final Event event;

    public VetoEvent(final Event event) {
        this.event = event;
    }

    public final Event getEvent() {
        return event;
    }
}
