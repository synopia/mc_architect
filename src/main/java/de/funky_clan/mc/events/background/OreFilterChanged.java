package de.funky_clan.mc.events.background;

import de.funky_clan.mc.eventbus.BackgroundEvent;
import de.funky_clan.mc.eventbus.Event;

/**
 * @author synopia
 */
public class OreFilterChanged implements BackgroundEvent {
    private boolean [] filter;

    public OreFilterChanged(boolean[] filter) {
        this.filter = filter;
    }

    public boolean[] getFilter() {
        return filter;
    }

}
