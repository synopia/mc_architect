package de.funky_clan.mc.model;

import java.awt.*;

/**
 * @author synopia
 */
public class SelectedBlock implements Renderable {
    private int x;
    private int y;
    private int thickness;

    public SelectedBlock() {
        thickness = 1;
    }

    public int getThickness() {
        return thickness;
    }

    public void setThickness(int thickness) {
        this.thickness = thickness;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public void render(RenderContext c) {
        Graphics2D g = c.getG();

        int sx = c.worldToPixelX(x);
        int sy = c.worldToPixelY(y);
        int w = c.worldToPixelX(1);
        int h = c.worldToPixelY(1);

        for (int i = 0; i < thickness; i++) {
            g.drawRect(sx - i, sy - i, w + 2 * i, h + 2 * i);
        }
    }
}
