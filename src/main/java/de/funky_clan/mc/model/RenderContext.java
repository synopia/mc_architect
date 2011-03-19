package de.funky_clan.mc.model;

//~--- non-JDK imports --------------------------------------------------------

import de.funky_clan.mc.config.Colors;
import de.funky_clan.mc.math.Point2i;
import de.funky_clan.mc.math.Position;

import java.awt.*;

//~--- JDK imports ------------------------------------------------------------

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

    private double pixelSizeX;
    private double pixelSizeY;
    private int screenSizeX;
    private int screenSizeY;

    private double windowSizeX;
    private double windowSizeY;
    private double windowPositionX;
    private double windowPositionY;

    private Position position;

    public RenderContext(Slice slice) {
        this.slice = slice;
        position = new Position();
        position.setSlice( slice );
        position.setRenderContext( this );
    }

    public void setGraphics( Graphics2D g ) {
        this.g = g;
    }

    public double screenToSliceX( int screenPosX ) {
        return screenPosX / pixelSizeX + windowPositionX;
    }
    public double screenToSliceY( int screenPosY ) {
        return screenPosY / pixelSizeY + windowPositionY;
    }

    public int sliceToScreenX( double sliceX ) {
        return (int) ( (sliceX-windowPositionX) * pixelSizeX );
    }
    public int sliceToScreenY( double sliceY ) {
        return (int) ( (sliceY-windowPositionY) * pixelSizeY );
    }


    public int screenUnitX( double a ) {
        int dx = (int) (a*pixelSizeX);
        return Math.max( 1, Math.abs(dx) ) + 1;
    }
    public int screenUnitY( double a ) {
        int dy = (int) (a*pixelSizeY);
        return Math.max( 1, Math.abs(dy) ) + 1;
    }

    public void setWindowPosition( double windowPositionX, double windowPositionY ) {
        this.windowPositionX = windowPositionX;
        this.windowPositionY = windowPositionY;
    }

    public void setWindowSize( double windowSizeX, double windowSizeY ) {
        this.windowSizeX = windowSizeX;
        this.windowSizeY = windowSizeY;
    }

    public void setScreenSize( int screenSizeX, int screenSizeY) {
        this.screenSizeX = screenSizeX;
        this.screenSizeY = screenSizeY;
    }

    public void init(  double windowPositionX, double windowPositionY , double windowSizeX, double windowSizeY , int screenSizeX, int screenSizeY ) {
        setWindowPosition(windowPositionX, windowPositionY);
        setWindowSize(windowSizeX, windowSizeY);
        setScreenSize(screenSizeX, screenSizeY);
        calculateSizes();
    }

    private void calculateSizes() {
        if( windowSizeX>0 && windowSizeY>0 ) {
            pixelSizeX = screenSizeX / windowSizeX;
            pixelSizeY = screenSizeY / windowSizeY;
        } else {
            pixelSizeX = 0;
            pixelSizeY = 0;
        }
    }

    public void zoom( double zoomX, double zoomY, Point mousePos ) {
        double midX = windowPositionX + windowSizeX / 2.;
        double midY = windowPositionY + windowSizeY / 2.;
        double posX = screenToSliceX(mousePos.x);
        double posY = screenToSliceY(mousePos.y);

        windowSizeX *= zoomX;
        windowSizeY *= zoomY;
        calculateSizes();
        windowPositionX += (posX-midX)*0.1;
        windowPositionY += (posY-midY)*0.1;
    }

    public Graphics2D getGraphics() {
        return g;
    }

    public double getPixelSizeX() {
        return pixelSizeX;
    }

    public double getPixelSizeY() {
        return pixelSizeY;
    }

    public int getScreenSizeX() {
        return screenSizeX;
    }

    public int getScreenSizeY() {
        return screenSizeY;
    }

    public double getWindowSizeX() {
        return windowSizeX;
    }

    public double getWindowSizeY() {
        return windowSizeY;
    }

    public double getWindowPositionX() {
        return windowPositionX;
    }

    public double getWindowPositionY() {
        return windowPositionY;
    }

    public int getWindowStartX() {
        return (int)windowPositionX;
    }
    public int getWindowStartY() {
        return (int)windowPositionY;
    }

    public int getWindowEndX() {
        return (int)(windowPositionX+windowSizeX);
    }
    public int getWindowEndY() {
        return (int)(windowPositionY+windowSizeY);
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
                windowPositionX, windowPositionY,
                windowSizeX, windowSizeY,
                screenSizeX, screenSizeY
        );
    }

    public int getCurrentSlice() {
        return slice.getSlice();
    }

    public Position getPosition() {
        return position;
    }
}
