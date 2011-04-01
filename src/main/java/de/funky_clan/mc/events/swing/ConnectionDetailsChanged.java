package de.funky_clan.mc.events.swing;

import de.funky_clan.mc.eventbus.Event;

/**
 * @author synopia
 */
public class ConnectionDetailsChanged implements Event {
    private String host;
    private int    listeningPort;
    private int    port;
    private String readableHost;

    public ConnectionDetailsChanged( int listeningPort, String readableHost ) {
        this.listeningPort = listeningPort;
        this.readableHost  = readableHost;

        if( readableHost.indexOf( ':' ) != -1 ) {
            String[] split = readableHost.split( ":", 2 );

            host = split[0];
            port = Integer.parseInt( split[1] );
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
