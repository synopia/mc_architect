package de.funky_clan.mc.events;

import de.funky_clan.mc.eventbus.Event;

/**
 * @author synopia
 */
public class OreFilterChanged implements Event {
    private boolean [] filter;

    public OreFilterChanged(boolean[] filter) {
        this.filter = filter;
    }

    public boolean[] getFilter() {
        return filter;
    }

    @Override
    public Object getChannel() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
