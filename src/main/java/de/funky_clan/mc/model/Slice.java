package de.funky_clan.mc.model;

import java.awt.*;

/**
 * @author synopia
 */
public class Slice implements Renderable {
    private int width;
    private int height;
    private int level;
    private int map[][];
    private BackgroundImage image;

    public Slice(int width, int height, int level) {
        this.width = width;
        this.height = height;
        this.level = level;

        map = new int[height][];
        for (int y = 0; y < height; y++) {
            map[y] = new int[width];
        }
    }

    public void setPixel(int x, int y, int value) {
        if (inRange(x, y)) {
            map[y][x] = value;
        }
    }

    public int getPixel(int x, int y) {
        int result = -1;
        if (inRange(x, y)) {
            result = map[y][x];
        }
        return result;
    }

    public BackgroundImage getImage() {
        return image;
    }

    public void setImage(BackgroundImage image) {
        this.image = image;
    }

    public boolean inRange(int x, int y) {
        return x >= 0 && y >= 0 && x < width && y < height;
    }

    public void render(RenderContext context) {
        if (image != null) {
            image.render(context);
        }

        Graphics2D g = context.getGraphics();

        for (int y = context.getStartY(); y < context.getEndY(); y++) {
            for (int x = context.getStartX(); x < context.getEndX(); x++) {
                int pixel = getPixel(x, y);
                if (pixel > 0) {

                    g.setColor(Color.DARK_GRAY);
                    g.fillRect(context.worldToPixelX(x), context.worldToPixelY(y), context.worldToPixelX(1) - 1, context.worldToPixelY(1) - 1);
                }
            }
        }
    }
}
