package de.funky_clan.mc.model;

import org.omg.CORBA.PRIVATE_MEMBER;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author synopia
 */
public class Chunk {
    private int                               map[][][];
    private int                               sizeX;
    private int                               sizeY;
    private int                               sizeZ;

    private int startX;
    private int startY;
    private int startZ;

    private Logger logger = LoggerFactory.getLogger(Chunk.class);
    private Model model;

    public Chunk(Model model, int startX, int startY, int startZ, int sizeX, int sizeY, int sizeZ) {
        this.model = model;
        this.startX = startX;
        this.startY = startY;
        this.startZ = startZ;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.sizeZ = sizeZ;

        map        = new int[sizeZ][][];

        for( int z = 0; z < sizeZ; z++ ) {
            map[z] = new int[sizeY][];

            for( int y = 0; y < sizeY; y++ ) {
                map[z][y] = new int[sizeX];
            }
        }
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

    public boolean isInRange( int x, int y, int z ) {
        return (x >= 0) && (y >= 0) && (z >= 0) && (x < sizeX) && (y < sizeY) && (z < sizeZ);
    }

    public void setPixelLocal( int x, int y, int z, int value ) {
        if( isInRange( x, y, z )) {
            map[z][y][x] = value;
        }
    }

    public int getPixelLocal( int x, int y, int z ) {
        int result = -1;

        if( isInRange( x, y, z )) {
            result = map[z][y][x];
        }

        return result;
    }

    public void setPixelGlobal( int x, int y, int z, int value ) {
        x -= startX;
        y -= startY;
        z -= startZ;

        if( isInRange( x, y, z )) {
            map[z][y][x] = value;
        }
    }

    public int getPixelGlobal( int x, int y, int z ) {
        x -= startX;
        y -= startY;
        z -= startZ;
        int result = -1;

        if( isInRange( x, y, z )) {
            result = map[z][y][x];
        }

        return result;
    }

}
