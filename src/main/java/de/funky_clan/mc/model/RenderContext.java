package de.funky_clan.mc.model;

//~--- non-JDK imports --------------------------------------------------------

import de.funky_clan.mc.config.Colors;
import de.funky_clan.mc.math.Point2d;
import de.funky_clan.mc.math.Point2i;
import de.funky_clan.mc.math.Point3d;
import de.funky_clan.mc.math.Point3i;

//~--- JDK imports ------------------------------------------------------------

import java.awt.*;

/**
 * Represents a viewport.
 *
 * A viewport
 *
 * @author synopia
 */
public class RenderContext {
    private Colors colors;
    private Graphics2D           g;
    private Slice slice;

    private Point2d pixelSize;
    private Point2i screenSize;

    private Point2d              windowSize;
    private Point2d              windowPosition;

    public RenderContext(Slice slice) {
        this.slice = slice;
    }

    public void setGraphics( Graphics2D g ) {
        this.g = g;
    }

    /**
     * Converts screen coordinate to world
     * @param screenPos
     * @return
     */
    public Point2d screenToSlice( Point2i screenPos ) {
        return new Point2d(
                screenPos.x() / pixelSize.x() + windowPosition.x(),
                screenPos.y() / pixelSize.y() + windowPosition.y()
        );
    }

    /**
     * Converts world coordinate to screen
     * @param worldPos
     * @return
     */
    public Point2i sliceToScreen( Point2d worldPos ) {
        return new Point2i(
                (int) ( (worldPos.x()-windowPosition.x()) * pixelSize.x() ),
                (int) ( (worldPos.y()-windowPosition.y()) * pixelSize.y() )
        );
    }

    public Point3d screenToWorld(Point2i screenPos) {
        Point2d slicePos = screenToSlice(screenPos);
        return slice.sliceToWorld(new Point3d(slicePos.x(), slicePos.y(), slice.getSlice()));
    }

    public Point2i worldToScreen(Point3d worldPos) {
        Point3d slicePos = slice.worldToSlice(worldPos);
        return sliceToScreen(new Point2d(slicePos.x(), slicePos.y()));
    }

    public Point3i sliceToWorld( Point3i slicedPos ) {
        return slice.sliceToWorld(slicedPos);
    }

    public Point3i worldToSlice( Point3i worldPos ) {
        return slice.worldToSlice(worldPos);
    }

    public Point3d sliceToWorld( Point3d slicedPos ) {
        return slice.sliceToWorld(slicedPos);
    }
    public Point3d worldToSlice( Point3d worldPos ) {
        return slice.worldToSlice(worldPos);
    }

    public Point2i screenUnit( Point2d worldPos ) {
        return screenUnit( worldPos, new Point2d(worldPos.x()+1, worldPos.y()+1) );
    }

    public Point2i screenUnit( Point2d worldPosA, Point2d worldPosB ) {
        Point2i a = sliceToScreen(worldPosA);
        Point2i b = sliceToScreen(worldPosB).sub(a);

        return new Point2i(
                Math.max( 1, Math.abs(b.x()) ) + 1,
                Math.max( 1, Math.abs(b.y()) ) + 1
        );
    }

    public void setWindowPosition( Point2d windowPos ) {
        windowPosition = windowPos;
    }

    public void setWindowSize( Point2d size ) {
        windowSize = size;
    }

    public void setScreenSize(Point2i screenSize) {
        this.screenSize = screenSize;
    }

    public void init( Point2d windowPos, Point2d windowSize, Point2i screenSize ) {
        setWindowPosition( windowPos );
        setWindowSize( windowSize );
        setScreenSize( screenSize );
        calculateSizes();
    }


    private void calculateSizes() {
        if( windowSize.x()>0 && windowSize.y()>0 ) {
            pixelSize = new Point2d(
                    screenSize.x() / windowSize.x(),
                    screenSize.y() / windowSize.y()
            );
        } else {
            pixelSize = new Point2d( 0, 0 );
        }
    }

    public void zoom( double zoomX, double zoomY, Point2i mousePos ) {
        Point2d mid = windowPosition.add(windowSize.scale(0.5, 0.5));
        Point2d pos = screenToSlice(mousePos);
        windowSize = new Point2d(windowSize.x() * zoomX, windowSize.y() * zoomY);
        calculateSizes();
        windowPosition = windowPosition.add(pos.sub(mid).scale(0.1, 0.1));
    }

    public Graphics2D getGraphics() {
        return g;
    }

    public Point2d getPixelSize() {
        return pixelSize;
    }

    public Point2i getScreenSize() {
        return screenSize;
    }

    public Point2d getWindowSize() {
        return windowSize;
    }

    public Point2d getWindowPosition() {
        return windowPosition;
    }

    public Point2i getWindowStart() {
        return new Point2i((int)windowPosition.x(), (int)windowPosition.y());
    }

    public Point2i getWindowEnd() {
        return new Point2i(
                (int)(windowPosition.x()+windowSize.x()),
                (int)(windowPosition.y()+windowSize.y())
        );
    }

    public Colors getColors() {
        return colors;
    }

    public void setColors( Colors colors ) {
        this.colors = colors;
    }

    @Override
    public String toString() {
        return String.format(
                "window = %.2f, %.2f; %.2f, %.2f - screen = %d, %d",
                windowPosition.x(), windowPosition.y(),
                windowSize.x(), windowSize.y(),
                screenSize.x(), screenSize.y()
        );
    }

    public int getCurrentSlice() {
        return slice.getSlice();
    }
}
