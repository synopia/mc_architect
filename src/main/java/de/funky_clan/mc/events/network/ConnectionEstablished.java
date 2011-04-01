package de.funky_clan.mc.events.network;

import de.funky_clan.mc.eventbus.Event;

/**
 * @author synopia
 */
public class ConnectionEstablished implements Event {
    private final String username;

    public ConnectionEstablished( String username ) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
