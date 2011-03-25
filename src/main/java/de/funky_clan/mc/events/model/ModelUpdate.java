package de.funky_clan.mc.events.model;

import de.funky_clan.mc.eventbus.Event;

/**
 * @author synopia
 */
public class ModelUpdate implements Event {
    private int chunkX;
    private int chunkZ;
    private int startX;
    private int startY;
    private int startZ;
    private int sizeX;
    private int sizeY;
    private int sizeZ;

    public ModelUpdate(int startX, int startY, int startZ, int sizeX, int sizeY, int sizeZ) {
        chunkX = startX>>4;
        chunkZ = startZ>>4;
        this.startX = startX;
        this.startY = startY;
        this.startZ = startZ;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.sizeZ = sizeZ;
    }

    public int getChunkX() {
        return chunkX;
    }

    public int getChunkZ() {
        return chunkZ;
    }

    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }

    public int getStartZ() {
        return startZ;
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
}
