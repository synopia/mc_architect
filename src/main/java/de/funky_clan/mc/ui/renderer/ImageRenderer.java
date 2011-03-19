package de.funky_clan.mc.ui.renderer;

import de.funky_clan.mc.math.Point2d;
import de.funky_clan.mc.model.BackgroundImage;
import de.funky_clan.mc.model.RenderContext;

import java.awt.*;

/**
 * @author synopia
 */
public class ImageRenderer implements Renderer<BackgroundImage> {
    @Override
    public void render(BackgroundImage object, RenderContext c) {
        Image image = object.getImage();
        Point2d start = object.getStart();
        Point2d size = object.getSize();

        if( image != null ) {
/*
            Point2i screenSize = c.getScreenSize();
            int width  = screenSize.x();
            int height = screenSize.y();

            Point2i from = c.sliceToScreen(start);
            Point2i to   = c.sliceToScreen(new Point2d(start.x() + size.x() - 1, start.y() + size.y()));

            int sx = from.x();
            int sy = from.y();
            int ex = to.x();
            int ey = to.y();

            int x1 = 0;
            int y1 = 0;
            int x2 = (int) size.x()-1;
            int y2 = (int) size.y()-1;

            if( ex>=0 && ey>=0 && sx<width && sy<height ) {
                Point2d startDiff = c.screenToWorld(new Point2i(sx, sy)).sub(c.screenToWorld(new Point2i(0,0)));
                Point2d endDiff   = c.screenToWorld(new Point2i(screenSize.x(), screenSize.y())).sub(c.screenToWorld(new Point2i(ex,ey)));

                if( sx<0 ) {
                    x1 += Math.abs(startDiff.x());
                    sx = 0;
                }
                if( sy<0 ) {
                    y1 += Math.abs(startDiff.y());
                    sy = 0;
                }
                if( ex>=width ) {
                    x2 -= Math.abs(endDiff.x());
                    ex = width-1;
                }
                if( ey>=height ) {
                    y2 -= Math.abs(endDiff.y());
                    ey = height-1;
                }
                c.getGraphics().drawImage( image, sx, sy, ex, ey, x1, y1, x2, y2, null );
                c.getGraphics().setColor(Color.BLUE);
                c.getGraphics().drawRect( sx, sy, ex-sx, ey-sy);
            }
*/

        }

    }
}
