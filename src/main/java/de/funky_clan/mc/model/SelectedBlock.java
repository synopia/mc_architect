package de.funky_clan.mc.model;

//~--- JDK imports ------------------------------------------------------------

import de.funky_clan.mc.math.Point2d;
import de.funky_clan.mc.math.Point2i;
import de.funky_clan.mc.math.Point3d;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;

import javax.swing.*;

/**
 * @author synopia
 */
public class SelectedBlock {
    public enum Type {
        ON_GRID,
        CENTERED
    }
    private Color color;
    private int   thickness;
    private Point3d position;
    private Type type = Type.ON_GRID;
    private final Logger log = LoggerFactory.getLogger(SelectedBlock.class);

    public SelectedBlock() {
        thickness = 2;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public int getThickness() {
        return thickness;
    }

    public void setThickness( int thickness ) {
        this.thickness = thickness;
    }

    public void setPosition(Point3d position) {
        this.position = position;
    }

    public Point3d getPosition() {
        return position;
    }

    // todo move this to Renderer
    public void repaint( JComponent component, RenderContext c ) {
        if( position!=null ) {
            Point2i from = c.sliceToScreen( new Point2d(position.x()-2, position.y()-2) );
            Point2i to =   c.sliceToScreen( new Point2d(position.x()+2, position.y()+2));

            // todo verify!
            component.repaint(from.x(), from.y(), to.x()-from.x(), to.y()-from.y());
        }
    }

    public Color getColor() {
        return color;
    }

    public void setColor( Color color ) {
        this.color = color;
    }
}
