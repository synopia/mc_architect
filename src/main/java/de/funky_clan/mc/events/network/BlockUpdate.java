package de.funky_clan.mc.events.network;

import de.funky_clan.mc.eventbus.Event;
import de.funky_clan.mc.eventbus.NetworkEvent;

/**
 * @author synopia
 */
public class BlockUpdate implements NetworkEvent {
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

}
