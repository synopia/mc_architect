package de.funky_clan.mc.events.swing;

/**
 * @author synopia
 */
public class MouseRectangle extends MouseMoved {
    private final int endX;
    private final int endY;
    private final int endZ;

    public MouseRectangle( int x, int y, int z, int endX, int endY, int endZ ) {
        super( x, y, z );
        this.endX = endX;
        this.endY = endY;
        this.endZ = endZ;
    }

    public int getEndX() {
        return endX;
    }

    public int getEndY() {
        return endY;
    }

    public int getEndZ() {
        return endZ;
    }
}
