package de.funky_clan.mc.events.swing;

import de.funky_clan.mc.eventbus.Event;
import de.funky_clan.mc.eventbus.SwingEvent;

/**
 * @author synopia
 */
public class ConnectionDetailsChanged implements SwingEvent {
    private String readableHost;
    private String host;
    private int port;
    private int listeningPort;

    public ConnectionDetailsChanged(String readableHost) {
        this.readableHost = readableHost;
        if( readableHost.indexOf(':')!=-1 ) {
            String[] split = readableHost.split(":", 2);
            host = split[0];
            port = Integer.parseInt(split[1]);
        } else {
            host = readableHost;
            port = 25565;
        }
    }

    public int getListeningPort() {
        return listeningPort;
    }

    public String getReadableHost() {
        return readableHost;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }
}
