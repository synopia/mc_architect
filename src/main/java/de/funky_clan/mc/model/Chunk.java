package de.funky_clan.mc.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * @author synopia
 */
public final class Chunk {
    private static final int CHUNK_ARRAY_SIZE = 16 * 16 * 128;
    private final byte map[];
    private final int sizeX;
    private final int sizeY;
    private final int sizeZ;
    private final int startX;
    private final int startY;
    private final int startZ;

    private Logger logger = LoggerFactory.getLogger(Chunk.class);

    public Chunk(final int startX, final int startY, final int startZ, final int sizeX, final int sizeY, final int sizeZ) {
        logger.info("Creating chunk "+startX+", "+startY+", "+startZ);

        this.startX = startX;
        this.startY = startY;
        this.startZ = startZ;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.sizeZ = sizeZ;

        map = new byte[CHUNK_ARRAY_SIZE];
    }

    public final int getStartX() {
        return startX;
    }

    public final int getStartY() {
        return startY;
    }

    public final int getStartZ() {
        return startZ;
    }

    public final int getSizeX() {
        return sizeX;
    }

    public final int getSizeY() {
        return sizeY;
    }

    public final int getSizeZ() {
        return sizeZ;
    }

    public final void setPixel(int x, int y, int z, int value) {
        int index = (y-startY) + ((z-startZ)*sizeY) + ((x-startX)*sizeY*sizeZ);
        map[index] = (byte) value;
    }

    public final int getPixel(int x, int y, int z) {
        int index = (y-startY) + ((z-startZ)*sizeY) + ((x-startX)*sizeY*sizeZ);
        return map[index];
    }

    public void updateFullBlock( byte[] data) {
        System.arraycopy(data, 0, map, 0, CHUNK_ARRAY_SIZE);
    }
}
