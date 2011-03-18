package de.funky_clan.mc.model;

import de.funky_clan.mc.ui.renderer.Renderer;

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

    public Ore(int startX, int startY, int startZ) {
        this( startX, startY, startZ, startX, startY, startZ);
    }

    public Ore(int startX, int startY, int startZ, int endX, int endY, int endZ) {
        this.startX = startX;
        this.startY = startY;
        this.startZ = startZ;
        this.endX = endX;
        this.endY = endY;
        this.endZ = endZ;
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

    public void addOre( int x, int y, int z ) {
        if( x<startX ) startX = x;
        if( y<startY ) startY = y;
        if( z<startZ ) startZ = z;
        if( x>endX )   endX = x;
        if( y>endY )   endY = y;
        if( z>endZ )   endZ = z;
    }

    public boolean contains( int x, int y, int z ) {
        return x>=startX && y>=startY && z>=startZ && x<=endX && y<=endY && z<=endZ;
    }

    public void addOre( Ore ore ) {
        addOre( ore.getStartX(), ore.getStartY(), ore.getStartZ() );
        addOre( ore.getEndX(), ore.getEndY(), ore.getEndZ() );
    }

    public void render(RenderContext c) {
        // draw selected block with player color
        //Point2d startScreen = c.worldToScreen( startWorld );
        //Point2d endScreen   = c.worldToScreen( endWorld );

        //..
    }
}
