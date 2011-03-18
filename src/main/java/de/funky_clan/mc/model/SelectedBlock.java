package de.funky_clan.mc.model;

//~--- JDK imports ------------------------------------------------------------

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;

import javax.swing.*;
import javax.vecmath.Point2d;
import javax.vecmath.Point2i;

/**
 * @author synopia
 */
public class SelectedBlock implements Renderable {
    private Color color;
    private int   thickness;
    private Point2d position = new Point2d();
    private final Logger log = LoggerFactory.getLogger(SelectedBlock.class);

    public SelectedBlock() {
        thickness = 2;
    }

    public int getThickness() {
        return thickness;
    }

    public void setThickness( int thickness ) {
        this.thickness = thickness;
    }

    public void setPosition(Point2d position) {
        this.position.set( position );
    }

    public Point2d getPosition() {
        return position;
    }

    @Override
    public void render( RenderContext c ) {
        Graphics2D g  = c.getGraphics();
        Point2i start = c.worldToScreen(position);
        Point2i size  = c.screenUnit(position);

        if( color == null ) {
            g.setColor( c.getColors().getSelectedBlockColor() );
        } else {
            g.setColor( color );
        }

        for( int i = 0; i < thickness; i++ ) {
            g.drawRect( c.getScreenSize().x-(start.x + i + 1), c.getScreenSize().y-(start.y + i + 1), size.x + 2 * i, size.y + 2 * i );
        }
    }

    // todo move this to Renderable
    public void repaint( JComponent component, RenderContext c ) {
        Point2i from = c.worldToScreen( new Point2d(position.x+2, position.y+2) );
        from.negate();
        from.add(c.getScreenSize());

        Point2i to = c.worldToScreen( new Point2d(position.x-2, position.y-2) );
        to.negate();
        to.add(c.getScreenSize());

        // todo verify!
        component.repaint(from.x, from.y, to.x-from.x, to.y-from.y);
    }

    public Color getColor() {
        return color;
    }

    public void setColor( Color color ) {
        this.color = color;
    }
}
