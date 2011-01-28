package de.funky_clan.mc.model;

import javax.swing.*;
import java.awt.*;

/**
 * @author paul.fritsche@googlemail.com
 */
public class Player extends SelectedBlock {
    private int direction;
    private int z;

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction+90;
    }

    @Override
    public void render(RenderContext c) {
        super.render(c);
        Graphics2D g = c.getGraphics();
        g.setColor(Color.BLUE.brighter());
        int sx = c.worldToPixelX(getX());
        int sy = c.worldToPixelY(getY());
        int w  = c.worldToPixelX(1);
        int h  = c.worldToPixelY(1);

        int mx = sx + w/2;
        int my = sy + h/2;

        int x1 = mx + (int)(10*w * Math.cos((direction-30) / 180.0 * Math.PI));
        int y1 = my + (int)(10*w * Math.sin((direction-30) / 180.0 * Math.PI));
        int x2 = mx + (int)(10*w * Math.cos((direction+30) / 180.0 * Math.PI));
        int y2 = my + (int)(10*w * Math.sin((direction+30) / 180.0 * Math.PI));

        g.drawLine( mx, my, x1, y1);
        g.drawLine( mx, my, x2, y2);
        g.drawLine( x1, y1, x2, y2);
    }

    public void repaint( JComponent component, RenderContext c) {
        component.repaint( c.worldToPixelX(getX()-15), c.worldToPixelX(getY()-15), c.worldToPixelX(30), c.worldToPixelY(30) );
    }

}
