package de.funky_clan.mc.events;

import de.funky_clan.mc.eventbus.Event;

/**
 * @author synopia
 */
public class ConnectionEstablished implements Event {
    private String username;

    public ConnectionEstablished(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public Object getChannel() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
