package de.funky_clan.mc.events.network;

import de.funky_clan.mc.eventbus.Event;
import de.funky_clan.mc.eventbus.NetworkEvent;

/**
 * @author synopia
 */
public class ConnectionEstablished implements NetworkEvent {
    private String username;

    public ConnectionEstablished(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
