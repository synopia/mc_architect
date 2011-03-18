package de.funky_clan.mc.model;

//~--- non-JDK imports --------------------------------------------------------

import de.funky_clan.mc.config.Colors;
import de.funky_clan.mc.math.Point2d;
import de.funky_clan.mc.math.Point2i;

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
    private Point2d pixelSize;
    private Point2i screenSize;

    private Point2d              windowSize;
    private Point2d              windowPosition;

    public RenderContext( ) {
    }

    public void setGraphics( Graphics2D g ) {
        this.g = g;
    }

    /**
     * Converts screen coordinate to world
     * @param screenPos
     * @return
     */
    public Point2d screenToWorld( Point2i screenPos ) {
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
    public Point2i worldToScreen( Point2d worldPos ) {
        return new Point2i(
                (int) ( (worldPos.x()-windowPosition.x()) * pixelSize.x() ),
                (int) ( (worldPos.y()-windowPosition.y()) * pixelSize.y() )
        );
    }

    public Point2i screenUnit( Point2d worldPos ) {
        return screenUnit( worldPos, new Point2d(worldPos.x()+1, worldPos.y()+1) );
    }

    public Point2i screenUnit( Point2d worldPosA, Point2d worldPosB ) {
        Point2i a = worldToScreen(worldPosA);
        Point2i b = worldToScreen(worldPosB).sub(a);

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

    public void zoom( double zoomX, double zoomY ) {
        windowSize = new Point2d(windowSize.x() * zoomX, windowSize.y() * zoomY);
        calculateSizes();
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
}
