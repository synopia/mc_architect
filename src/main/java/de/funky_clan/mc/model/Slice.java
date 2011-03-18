package de.funky_clan.mc.model;

//~--- JDK imports ------------------------------------------------------------

import de.funky_clan.mc.config.DataValues;
import de.funky_clan.mc.math.Point2d;
import de.funky_clan.mc.math.Point2i;
import de.funky_clan.mc.math.Point3d;
import de.funky_clan.mc.math.Point3i;

import java.awt.*;

/**
 * @author synopia
 */
public class Slice implements Renderable {
    private Model     model;
    private int       slice;
    private SliceType type;

    public Slice( Model model, SliceType type ) {
        this.model = model;
        this.type  = type;
    }

    public void setSlice( int slice ) {
        this.slice = slice;
    }

    public Point3i sliceToWorld( Point3i slicedPos ) {
        Point3i result = new Point3i();
        switch (type) {
            case X:
                result.set( slicedPos.z(), slicedPos.y(), slicedPos.x() );
                break;
            case Y:
                result.set( slicedPos.x(), slicedPos.y(), slicedPos.z() );
                break;
            case Z:
                result.set( slicedPos.x(), slicedPos.z(), slicedPos.y() );
                break;
        }
        return result;
    }

    public Point3i worldToSlice( Point3i worldPos ) {
        return  sliceToWorld(worldPos);
    }

    public Point3d sliceToWorld( Point3d slicedPos ) {
        Point3d result = new Point3d();
        switch (type) {
            case X:
                result.set( slicedPos.z(), slicedPos.y(), slicedPos.x() );
                break;
            case Y:
                result.set( slicedPos.x(), slicedPos.y(), slicedPos.z() );
                break;
            case Z:
                result.set( slicedPos.x(), slicedPos.z(), slicedPos.y() );
                break;
        }
        return result;
    }

    public Point3d worldToSlice( Point3d worldPos ) {
        return  sliceToWorld(worldPos);
    }

    public int getPixel(int x, int y, PixelType type) {
        Point3i map = sliceToWorld( new Point3i(x, y, slice) );
        return model.getPixel( map.x(), map.y(), map.z(), type);
    }

    public void setPixel(int x, int y, int slice, PixelType type, int value) {
        Point3i map = sliceToWorld(new Point3i(x, y, slice));
        model.setPixel(map.x(), map.y(), map.z(), value, type);
    }

    public void render( RenderContext context ) {
        Graphics2D g  = context.getGraphics();
        int        sx = context.getWindowStart().x() - 1;
        int        sy = context.getWindowStart().y() - 1;
        int        ex = context.getWindowEnd().x() + 2;
        int        ey = context.getWindowEnd().y() + 2;

        for( int y = sy; y < ey; y++ ) {
            for( int x = sx; x < ex; x++ ) {
                int blockId = getPixel( x, y, PixelType.BLOCK_ID );
                int blueprint = getPixel( x, y, PixelType.BLUEPRINT );

                Color colorForBlock = null;

                if( blockId > 0 ) {
                    colorForBlock = context.getColors().getColorForBlock(blockId);
                }
                if( blueprint==1 ) {
                    if( colorForBlock==null ) {
                        colorForBlock = context.getColors().getColorForBlock(DataValues.AIR.getId());
                    }
                    colorForBlock = colorForBlock.darker().darker().darker();
                }

                if( colorForBlock!=null ) {
                    g.setColor(colorForBlock);
                    Point2d position = new Point2d(x, y);
                    Point2i curr = context.worldToScreen(position);
                    Point2i size = context.screenUnit(position);

                    g.fillRect( curr.x(), curr.y(), size.x(), size.y());
                }
            }
        }
    }

    public SliceType getType() {
        return type;
    }
}
