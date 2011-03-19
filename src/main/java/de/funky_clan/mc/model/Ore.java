package de.funky_clan.mc.model;

/**
 * @author synopia
 */
public class Ore {
    private int startX;
    private int startY;
    private int startZ;
    private int endX;
    private int endY;
    private int endZ;

    public Ore(int startX, int startY, int startZ, int endX, int endY, int endZ) {
        this.startX = startX;
        this.startY = startY;
        this.startZ = startZ;
        this.endX = endX;
        this.endY = endY;
        this.endZ = endZ;
    }

    public Ore(int startX, int startY, int startZ) {
        this( startX, startY, startZ, startX, startY, startZ );
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

    public int getEndX() {
        return endX;
    }

    public int getEndY() {
        return endY;
    }

    public int getEndZ() {
        return endZ;
    }

    public void addOre( int x, int y, int z) {
        startX = Math.min( startX, x );
        startY = Math.min( startY, y );
        startZ = Math.min( startZ, z );

        endX = Math.max(endX, x);
        endY = Math.max(endY, y);
        endZ = Math.max( endZ, z );
    }

    public boolean contains(int x, int y, int z) {
        return x>=startX && y>=startY && z>=startZ && x<=endX && y<=endY && z<=endZ;
    }

    public void addOre( Ore ore ) {
        addOre( ore.getStartX(), ore.getStartY(), ore.getStartZ() );
        addOre( ore.getEndX(), ore.getEndY(), ore.getEndZ() );
    }
}
