package de.funky_clan.mc.events.swing;

import de.funky_clan.mc.eventbus.Event;

/**
 * @author synopia
 */
public abstract class MouseEvent implements Event {
    private int x;
    private int y;
    private int z;

    protected MouseEvent( int x, int y, int z ) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }
}
