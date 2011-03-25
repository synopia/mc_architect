package de.funky_clan.mc.eventbus;

/**
 * @author synopia
 */
public interface VetoHandler<E extends Event> {
    boolean isVeto( E event );

    void handleVeto( E event );
}
