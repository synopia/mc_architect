package de.funky_clan.mc.model;

import java.awt.*;

/**
 * @author synopia
 */
public class RenderContext {
    private Graphics2D g;
    private Model model;
    private int startX;
    private int startY;
    private int width;
    private int height;
    private int windowWidth;
    private int windowHeight;
    private float pixelSizeX;
    private float pixelSizeY;

    public RenderContext(Model model) {
        this.model = model;
    }

    public void setG(Graphics2D g) {
        this.g = g;
    }

    public int worldToPixelX(int x) {
        return (int) (x * pixelSizeX);
    }

    public int worldToPixelY(int y) {
        return (int) (y * pixelSizeY);
    }

    public void setWindowSize(int width, int height) {
        windowWidth = width;
        windowHeight = height;
        setPixelSize((float) width / model.getWidth(), (float) height / model.getHeight());
    }

    public int getWindowWidth() {
        return windowWidth;
    }

    public int getWindowHeight() {
        return windowHeight;
    }

    public void setPixelSize(float x, float y) {
        pixelSizeX = x;
        pixelSizeY = y;
    }


    public Graphics2D getG() {
        return g;
    }

    public Model getModel() {
        return model;
    }

    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setStartPosition(int x, int y) {
        startX = x;
        startY = y;
    }


    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getEndX() {
        return startX + width;
    }

    public int getEndY() {
        return startY + height;
    }
}
