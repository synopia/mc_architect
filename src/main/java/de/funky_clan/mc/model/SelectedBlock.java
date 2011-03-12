package de.funky_clan.mc.model;

//~--- JDK imports ------------------------------------------------------------

import java.awt.*;

import javax.swing.*;

/**
 * @author synopia
 */
public class SelectedBlock implements Renderable {
    private Color color;
    private int   thickness;
    private int   x;
    private int   y;

    public SelectedBlock() {
        thickness = 2;
    }

    public int getThickness() {
        return thickness;
    }

    public void setThickness( int thickness ) {
        this.thickness = thickness;
    }

    public int getX() {
        return x;
    }

    public void setX( int x ) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY( int y ) {
        this.y = y;
    }

    @Override
    public void render( RenderContext c ) {
        Graphics2D g  = c.getGraphics();
        int        sx = c.modelToScreenX(x);
        int        sy = c.modelToScreenY(y);
        int        w  = c.screenUnitX(x);
        int        h  = c.screenUnitY(y);

        if( color == null ) {
            g.setColor( c.getColors().getSelectedBlockColor() );
        } else {
            g.setColor( color );
        }

        for( int i = 0; i < thickness; i++ ) {
            g.drawRect( sx - i - 1, sy - i - 1, w + 2 * i, h + 2 * i );
        }
    }

    // todo move this to Renderable
    public void repaint( JComponent component, RenderContext c ) {
        component.repaint( c.modelToScreenX(getX() - 2), c.modelToScreenX(getY() - 2), c.screenUnitX(getX()-2, getX()+2),
                           c.screenUnitY(getY()-2, getY()+2));
    }

    public Color getColor() {
        return color;
    }

    public void setColor( Color color ) {
        this.color = color;
    }
}
