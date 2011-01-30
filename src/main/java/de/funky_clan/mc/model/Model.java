package de.funky_clan.mc.model;

/**
 * @author synopia
 */
public class Model {
    private int sizeX;
    private int sizeY;
    private int sizeZ;

    private int map[][][];

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

    public Slice getXSlice(int slice) {
        return new Slice(this, Slice.SliceType.X, slice);
    }
    public Slice getYSlice(int slice) {
        return new Slice(this, Slice.SliceType.Y, slice);
    }
    public Slice getZSlice(int slice) {
        return new Slice(this, Slice.SliceType.Z, slice);
    }
}
