package de.funky_clan.mc.model;

//~--- non-JDK imports --------------------------------------------------------

import de.funky_clan.mc.config.Configuration;

//~--- JDK imports ------------------------------------------------------------

import java.awt.*;

/**
 * Helps transforming coordinates between background screen, the actual visible window and the world model.
 *
 * @author synopia
 */
public class RenderContext {
    private Configuration.Colors colors;
    private Graphics2D           g;
    private double               pixelSizeX;
    private double               pixelSizeY;
    private int                  screenHeight;
    private int                  screenWidth;

    private double               windowHeight;
    private double               windowWidth;
    private double               windowX;
    private double               windowY;

    public RenderContext( ) {
    }

    public void setGraphics( Graphics2D g ) {
        this.g = g;
    }

    /**
     * Converts pixel coordinate to world coordinate
     *
     * @param x x value of coordinate in pixels
     * @return x value of coordinate in world-x (no range check!)
     */
    public double screenToModelX(int x) {
        return x / pixelSizeX + windowX;
    }

    /**
     * Converts pixel coordinate to world coordinate
     *
     * @param y y value of coordinate in pixels
     * @return y value of coordinate in world-y (no range check!)
     */
    public double screenToModelY(int y) {
        return  y / pixelSizeY + windowY;
    }

    /**
     * Converts world coordinate to pixel.
     *
     * @param x x value of world coordinate
     * @return x value of coordinate in pixels (top left corner of block)
     */
    public int modelToScreenX(double x) {
        return(int) ( (x-windowX) * pixelSizeX );
    }

    /**
     * Converts world coordinate to pixel.
     *
     * @param y y value of world coordinate
     * @return y value of coordinate in pixels (top left corner of block)
     */
    public int modelToScreenY(double y) {
        return (int) ( (y-windowY) * pixelSizeY );
    }

    public int screenUnitX(int x) {
        return screenUnitX( x, x+1 );
    }
    public int screenUnitY(int y) {
        return screenUnitY( y, y+1 );
    }

    public int screenUnitX(int x1, int x2) {
        return Math.max( 1, Math.abs(modelToScreenX(x1)-modelToScreenX(x2))-1 );
    }
    public int screenUnitY(int y1, int y2) {
        return Math.max( 1, Math.abs(modelToScreenY(y1) - modelToScreenY(y2)-1 ) );
    }

    /**
     * Top left corner of viewport window
     *
     * @param x coordinate
     * @param y coordinate
     */
    public void setWindowStart( double x, double y ) {
        windowX = x;
        windowY = y;
    }

    /**
     * Size of viewport window
     *
     * @param width  of window
     * @param height of window
     */
    public void setWindowSize( double width, double  height ) {
        windowWidth  = width;
        windowHeight = height;
        calculateSizes();
    }

    /**
     * Scaled size, where content is drawn to
     *
     */
    public void init(double windowX, double windowY, double windowWidth, double windowHeight, int screenWidth, int screenHeight) {
        this.windowX = windowX;
        this.windowY = windowY;
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        calculateSizes();
    }

    private void calculateSizes() {
        if( windowWidth>0 && windowHeight>0 ) {
            setPixelSize(screenWidth / windowWidth, screenHeight / windowHeight);
        } else {
            setPixelSize(0,0);
        }
    }

    public void zoom( double zoomX, double zoomY ) {
        init(windowX, windowY, windowWidth * zoomX, windowHeight * zoomY, screenWidth, screenHeight);
    }

    /**
     * @return width of the screen (equals model's width if no zoom) in pixels
     */
    public int getScreenWidth() {
        return screenWidth;
    }

    /**
     * @return height of the screen (equals model's height if no zoom) in pixels
     */
    public int getScreenHeight() {
        return screenHeight;
    }

    public Graphics2D getGraphics() {
        return g;
    }

    /**
     * @return top left corner of viewport window in model space
     */
    public int getStartX() {
        return (int)windowX;
    }

    /**
     * @return top left corner of viewport window in model space
     */
    public int getStartY() {
        return (int)windowY;
    }

    /**
     * @return width of viewport window in model space
     */
    public int getWidth() {
        return (int)windowWidth;
    }

    /**
     * @return width of viewport window in model space
     */
    public int getHeight() {
        return (int)windowHeight;
    }

    /**
     * @return bottom right corner of viewport window in model space
     */
    public int getEndX() {
        return getStartX() + getWidth();
    }

    /**
     * @return bottom right corner of viewport window in model space
     */
    public int getEndY() {
        return getStartY() + getHeight();
    }

    public double getWindowX() {
        return windowX;
    }

    public double getWindowY() {
        return windowY;
    }

    public double getWindowWidth() {
        return windowWidth;
    }

    public double getWindowHeight() {
        return windowHeight;
    }

    protected void setPixelSize( double x, double y ) {
        pixelSizeX = x;
        pixelSizeY = y;
    }

    public double getPixelSizeX() {
        return pixelSizeX;
    }

    public double getPixelSizeY() {
        return pixelSizeY;
    }

    public Configuration.Colors getColors() {
        return colors;
    }

    public void setColors( Configuration.Colors colors ) {
        this.colors = colors;
    }

    public void setScreenSize(int width, int height) {
        this.screenWidth = width;
        this.screenHeight = height;

        calculateSizes();
    }

    @Override
    public String toString() {
        return String.format("window = %.2f, %.2f; %.2f, %.2f - screen = %d, %d", windowX, windowY, windowWidth, windowHeight, screenWidth, screenHeight);
    }
}
