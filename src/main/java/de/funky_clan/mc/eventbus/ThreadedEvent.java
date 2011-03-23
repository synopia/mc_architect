package de.funky_clan.mc.eventbus;

/**
 * @author synopia
 */
public interface ThreadedEvent extends Event {
    Object getTopic();
}
