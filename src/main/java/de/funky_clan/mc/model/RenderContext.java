package de.funky_clan.mc.model;

import de.funky_clan.mc.config.Colors;
import de.funky_clan.mc.math.Position;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Graphics2D;
import java.awt.Point;

/**
 * Represents a viewport.
 *
 * A viewport
 *
 * @author synopia
 */
public class RenderContext {
    private final Logger logger    = LoggerFactory.getLogger( RenderContext.class );
    private Box          windowBox = new Box();
    private Colors       colors;
    private Graphics2D   g;
    private double       pixelSizeX;
    private double       pixelSizeY;
    private Position     position;
    private int          screenSizeX;
    private int          screenSizeY;
    private Slice        slice;
    private Position     windowEnd;
    private double       windowPositionX;
    private double       windowPositionY;
    private double       windowSizeX;
    private double       windowSizeY;
    private Position     windowStart;

    public RenderContext( Slice slice ) {
        this.slice = slice;
        position   = new Position();
        position.setSlice( slice );
        position.setRenderContext( this );
        windowStart = new Position();
        windowStart.setSlice( slice );
        windowStart.setRenderContext( this );
        windowEnd = new Position();
        windowEnd.setSlice( slice );
        windowEnd.setRenderContext( this );
    }

    public void updateWindowBox() {
        int minSlice = 0;
        int maxSlice = slice.getSlice();

        if( slice.getType() != SliceType.Y ) {
            minSlice = slice.getSlice() - 20;
            maxSlice = slice.getSlice() + 20;
        }

        windowStart.setSlice( windowPositionX, windowPositionY, minSlice );
        windowEnd.setSlice( windowPositionX + windowSizeX, windowPositionY + windowSizeY, maxSlice );
        windowBox.set( windowStart.getWorldX(), windowStart.getWorldY(), windowStart.getWorldZ(),
                       windowEnd.getWorldX(), windowEnd.getWorldY(), windowEnd.getWorldZ() );
    }

    public boolean contains( double worldX, double worldY, double worldZ ) {
        return windowBox.contains( worldX, worldY, worldZ );
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
        return(int) (( sliceX - windowPositionX ) * pixelSizeX );
    }

    public int sliceToScreenY( double sliceY ) {
        return(int) (( sliceY - windowPositionY ) * pixelSizeY );
    }

    public int screenUnitX( double a ) {
        int dx = (int) ( a * pixelSizeX );

        return Math.max( 1, Math.abs( dx ));
    }

    public int screenUnitY( double a ) {
        int dy = (int) ( a * pixelSizeY );

        return Math.max( 1, Math.abs( dy ));
    }

    public void setWindowPosition( double windowPositionX, double windowPositionY ) {
        this.windowPositionX = windowPositionX;
        this.windowPositionY = windowPositionY;
    }

    public void setWindowSize( double windowSizeX, double windowSizeY ) {
        this.windowSizeX = windowSizeX;
        this.windowSizeY = windowSizeY;
    }

    public void setScreenSize( int screenSizeX, int screenSizeY ) {
        this.screenSizeX = screenSizeX;
        this.screenSizeY = screenSizeY;
    }

    public void init( double windowPositionX, double windowPositionY, double windowSizeX, double windowSizeY,
                      int screenSizeX, int screenSizeY ) {
        setWindowPosition( windowPositionX, windowPositionY );
        setWindowSize( windowSizeX, windowSizeY );
        setScreenSize( screenSizeX, screenSizeY );
        calculateSizes();
    }

    public void calculateSizes() {
        if( windowSizeX > windowSizeY ) {
            windowSizeX = windowSizeY;
        }

        if( windowSizeX < windowSizeY ) {
            windowSizeY = windowSizeX;
        }

        if(( screenSizeX != 0 ) && ( screenSizeY != 0 )) {
            if( screenSizeX > screenSizeY ) {
                windowSizeX *= (double) screenSizeX / screenSizeY;
            } else {
                windowSizeY *= (double) screenSizeY / screenSizeX;
            }
        }

        if(( windowSizeX > 0 ) && ( windowSizeY > 0 )) {
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
        double posX = screenToSliceX( mousePos.x );
        double posY = screenToSliceY( mousePos.y );

        windowSizeX *= zoomX;
        windowSizeY *= zoomY;
        calculateSizes();
        windowPositionX += ( posX - midX ) * 0.1;
        windowPositionY += ( posY - midY ) * 0.1;
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
        return(int) windowPositionX;
    }

    public int getWindowStartY() {
        return(int) windowPositionY;
    }

    public int getWindowEndX() {
        return(int) ( windowPositionX + windowSizeX );
    }

    public int getWindowEndY() {
        return(int) ( windowPositionY + windowSizeY );
    }

    public Colors getColors() {
        return colors;
    }

    public void setColors( Colors colors ) {
        this.colors = colors;
    }

    @Override
    public String toString() {
        return String.format( "window = %.2f, %.2f; %.2f, %.2f - screen = %d, %d", windowPositionX, windowPositionY,
                              windowSizeX, windowSizeY, screenSizeX, screenSizeY );
    }

    public int getCurrentSlice() {
        return slice.getSlice();
    }

    public Position getPosition() {
        return position;
    }
}
