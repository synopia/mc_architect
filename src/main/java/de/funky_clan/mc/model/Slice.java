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

        int sx = context.getStartX()-1;
        int sy = context.getStartY()-1;
        int ex = context.getEndX()+2;
        int ey = context.getEndY()+2;

        for (int y = sy; y < ey; y++) {
            for (int x = sx; x < ex; x++) {
                int pixel = getPixel(x, y);
                if (pixel > 0) {

                    g.setColor(Color.DARK_GRAY);
                    int next_x = context.worldToPixelX(x+1);
                    int next_y = context.worldToPixelY(y+1);
                    int curr_x = context.worldToPixelX(x);
                    int curr_y = context.worldToPixelY(y);

                    g.fillRect(curr_x, curr_y, next_x-curr_x-1, next_y-curr_y-1);
                }
            }
        }
    }

}
