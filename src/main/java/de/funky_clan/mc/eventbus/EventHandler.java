package de.funky_clan.mc.eventbus;

/**
 * @author synopia
 */
public interface EventHandler<E extends Event> {
    void handleEvent( E event );
}
