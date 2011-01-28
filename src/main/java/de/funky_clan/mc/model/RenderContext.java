package de.funky_clan.mc.model;

import java.awt.*;

/**
 * Holds and transform model to pixel coordinates.
 *
 * @author synopia
 */
public class RenderContext {
    private Graphics2D g;

    private Model model;
    private int screenWidth;
    private int screenHeight;
    private float pixelSizeX;
    private float pixelSizeY;

    private int windowX;
    private int windowY;
    private int windowWidth;
    private int windowHeight;

    public RenderContext(Model model) {
        this.model = model;
    }

    public void setGraphics(Graphics2D g) {
        this.g = g;
    }

    /**
     * Converts pixel coordinate to world coordinate
     *
     * @param x x value of coordinate in pixels
     * @return x value of coordinate in world-x (no range check!)
     */
    public int pixelToWorldX(int x) {
        return (int) (x / pixelSizeX);
    }

    /**
     * Converts pixel coordinate to world coordinate
     *
     * @param y y value of coordinate in pixels
     * @return y value of coordinate in world-y (no range check!)
     */
    public int pixelToWorldY(int y) {
        return (int) (y / pixelSizeY);
    }

    /**
     * Converts world coordinate to pixel.
     *
     * @param x x value of world coordinate
     * @return x value of coordinate in pixels (top left corner of block)
     */
    public int worldToPixelX(int x) {
        return (int) (x * pixelSizeX);
    }

    /**
     * Converts world coordinate to pixel.
     *
     * @param y y value of world coordinate
     * @return y value of coordinate in pixels (top left corner of block)
     */
    public int worldToPixelY(int y) {
        return (int) (y * pixelSizeY);
    }

    /**
     * Top left corner of viewport window
     *
     * @param x coordinate
     * @param y coordinate
     */
    public void setWindowStart( int x, int y ) {
        windowX = x;
        windowY = y;
    }

    /**
     * Size of viewport window
     *
     * @param width  of window
     * @param height of window
     */
    public void setWindowSize( int width, int height ) {
        windowWidth  = width;
        windowHeight = height;
    }

    /**
     * Scaled size, where content is drawn to
     *
     * @param width  of screen
     * @param height of screen
     */
    public void setScreenSize(int width, int height) {
        screenWidth = width;
        screenHeight = height;
        setPixelSize((float) width / getModelWidth(), (float) height / getModelHeight());
    }

    public int getModelWidth() {
        return model.getWidth();
    }

    public int getModelHeight() {
        return model.getHeight();
    }

    /**
     * @return width of the screen (equals model's width if no zoom)
     */
    public int getScreenWidth() {
        return screenWidth;
    }

    /**
     * @return height of the screen (equals model's height if no zoom)
     */
    public int getScreenHeight() {
        return screenHeight;
    }

    public Graphics2D getGraphics() {
        return g;
    }

    public Model getModel() {
        return model;
    }

    /**
     * @return top left corner of viewport window in model space
     */
    public int getStartX() {
        return pixelToWorldX(windowX);
    }

    /**
     * @return top left corner of viewport window in model space
     */
    public int getStartY() {
        return pixelToWorldY(windowY);
    }

    /**
     * @return width of viewport window in model space
     */
    public int getWidth() {
        return pixelToWorldX(windowWidth);
    }

    /**
     * @return width of viewport window in model space
     */
    public int getHeight() {
        return pixelToWorldY(windowHeight);
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

    protected void setPixelSize(float x, float y) {
        pixelSizeX = x;
        pixelSizeY = y;
    }

    public float getPixelSizeX() {
        return pixelSizeX;
    }

    public float getPixelSizeY() {
        return pixelSizeY;
    }
}
