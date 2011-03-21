package de.funky_clan.mc.events.mouse;

/**
 * @author synopia
 */
public class MouseRectangle extends MouseMoved {
    private int sizeX;
    private int sizeY;
    private int sizeZ;

    public MouseRectangle(int x, int y, int z, int sizeX, int sizeY, int sizeZ) {
        super(x, y, z);
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.sizeZ = sizeZ;
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
