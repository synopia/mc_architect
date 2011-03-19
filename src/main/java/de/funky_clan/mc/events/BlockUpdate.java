package de.funky_clan.mc.events;

import de.funky_clan.mc.eventbus.Event;

/**
 * @author synopia
 */
public class BlockUpdate implements Event {
    private int x;
    private int y;
    private int z;
    private byte type;
    private byte meta;

    public BlockUpdate(int x, int y, int z, byte type, byte meta) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.type = type;
        this.meta = meta;
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

    public byte getType() {
        return type;
    }

    public byte getMeta() {
        return meta;
    }

    @Override
    public Object getChannel() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
