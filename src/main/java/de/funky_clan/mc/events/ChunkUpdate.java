package de.funky_clan.mc.events;

import de.funky_clan.mc.eventbus.Event;

/**
 * @author synopia
 */
public class ChunkUpdate implements Event {
    private int sx;
    private int sy;
    private int sz;
    private int sizeX;
    private int sizeY;
    private int sizeZ;
    private byte[] data;

    public ChunkUpdate(int sx, int sy, int sz, int sizeX, int sizeY, int sizeZ, byte[] data) {
        this.sx = sx;
        this.sy = sy;
        this.sz = sz;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.sizeZ = sizeZ;
        this.data = data;
    }

    @Override
    public Object getChannel() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public int getSx() {
        return sx;
    }

    public int getSy() {
        return sy;
    }

    public int getSz() {
        return sz;
    }

    public int getSizeX() {
        return sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }

    public int getSizeZ() {
        return sizeZ;
    }

    public byte[] getData() {
        return data;
    }
}
