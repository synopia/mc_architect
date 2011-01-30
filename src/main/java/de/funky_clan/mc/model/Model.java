package de.funky_clan.mc.model;

import java.util.HashMap;

/**
 * @author synopia
 */
public class Model {
    private int sizeX;
    private int sizeY;
    private int sizeZ;

    private int map[][][];
    private HashMap<Integer, BackgroundImage> zSliceImages = new HashMap<Integer, BackgroundImage>();

    public Model(int sizeX, int sizeY, int sizeZ) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.sizeZ = sizeZ;

        map = new int[sizeZ][][];
        for (int z = 0; z < sizeZ; z++) {
            map[z] = new int[sizeY][];
            for (int y = 0; y < sizeY; y++) {
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
        return x >= 0 && y >= 0 && z>=0 && x < sizeX && y < sizeY && z < sizeZ;
    }

    public void setPixel( int x, int y, int z, int value ) {
        if( isInRange( x, y, z) ) {
            map[z][y][x] = value;
        }
    }

    public int getPixel( int x, int y, int z ) {
        int result = -1;

        if( isInRange(x, y, z) ) {
            result = map[z][y][x];
        }
        return result;
    }

    public void addImage(Slice.SliceType type, int slice, BackgroundImage image ) {
        zSliceImages.put(slice, image);
    }

    public BackgroundImage getImage(Slice.SliceType type, int slice) {
        BackgroundImage result;
        switch (type) {
            case Z:
                result = zSliceImages.get(slice);
                break;
            default:
                result = null;
        }
        return result;
    }
}
