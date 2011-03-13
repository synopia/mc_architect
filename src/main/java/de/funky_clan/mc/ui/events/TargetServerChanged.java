package de.funky_clan.mc.ui.events;

import de.funky_clan.mc.eventbus.Event;

/**
 * @author synopia
 */
public class TargetServerChanged implements Event {
    private String readableHost;
    private String host;
    private int port;

    public TargetServerChanged(String readableHost) {
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

    public String getReadableHost() {
        return readableHost;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    @Override
    public Object getChannel() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
