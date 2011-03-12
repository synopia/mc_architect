package de.funky_clan.mc.model;

//~--- JDK imports ------------------------------------------------------------

import java.awt.*;

/**
 * @author synopia
 */
public class Slice implements Renderable {
    private Model     model;
    private int       slice;
    private int       slices;
    private SliceType type;

    public enum SliceType {
        X,    // y-z
        Y,    // x-z
        Z,    // x-y
    }

    public Slice( Model model, SliceType type ) {
        this.model = model;
        this.type  = type;
    }

    public void setSlice( int slice ) {
        this.slice = slice;
    }



    public int getSlices() {
        return slices;
    }

    public int[] mapSliceToWorld( int x, int y, int slice ) {
        int mapX = -1,
            mapY = -1,
            mapZ = -1;

        switch( type ) {
        case X:
            mapX = slice;
            mapZ = x;
            mapY = y;

            break;

        case Y:
            mapY = y;
            mapZ = slice;
            mapX = x;

            break;

        case Z:
            mapZ = y;
            mapX = x;
            mapY = slice;

            break;
        }

        return new int[] {mapX, mapY, mapZ};
    }

    public int[] mapWorldToSlice( int x, int y, int z ) {
        int wx = -1,
            wy = -1,
            wz = -1;

        switch( getType() ) {
        case X:
            wx = z;
            wy = y;
            wz = x;

            break;

        case Y:
            wx = x;
            wy = y;
            wz = z;

            break;

        case Z:
            wx = x;
            wy = z;
            wz = y;

            break;
        }

        return new int[] {wx, wy, wz};
    }

    public int getPixel( int x, int y ) {
        int[] map = mapSliceToWorld( x, y, slice );

        return model.getPixel( map[0], map[1], map[2] );
    }

    public void render( RenderContext context ) {
        Graphics2D g  = context.getGraphics();
        int        sx = context.getStartX() - 1;
        int        sy = context.getStartY() - 1;
        int        ex = context.getEndX() + 2;
        int        ey = context.getEndY() + 2;

        for( int y = sy; y < ey; y++ ) {
            for( int x = sx; x < ex; x++ ) {
                int pixel = getPixel( x, y );

                if( pixel > 0 ) {
                    g.setColor( context.getColors().getBlockColor() );

                    int next_x = context.modelToScreenX(x + 1);
                    int next_y = context.modelToScreenY(y + 1);
                    int curr_x = context.modelToScreenX(x);
                    int curr_y = context.modelToScreenY(y);

                    g.fillRect( curr_x, curr_y, Math.max(next_x - curr_x - 1, 1), Math.max(next_y - curr_y - 1,1) );
                }
            }
        }
    }

    public SliceType getType() {
        return type;
    }
}
