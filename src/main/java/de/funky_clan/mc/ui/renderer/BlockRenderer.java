package de.funky_clan.mc.ui.renderer;

import com.sun.org.apache.xpath.internal.operations.Minus;
import de.funky_clan.mc.math.Point2i;
import de.funky_clan.mc.math.Point3d;
import de.funky_clan.mc.model.RenderContext;
import de.funky_clan.mc.model.SelectedBlock;

import java.awt.*;

/**
 * @author synopia
 */
public class BlockRenderer<B extends SelectedBlock> implements Renderer<B> {
    @Override
    public void render(B object, RenderContext c) {
        Graphics2D g  = c.getGraphics();

        Point3d position = object.getPosition();
        if( position!=null ) {
            Point2i start = c.worldToScreen(position);
            Point2i size  = c.screenUnit(position);
            if( object.getType()==SelectedBlock.Type.CENTERED ) {
                start = start.sub(new Point2i(size.x()/2, size.y()/2) );
            }

            Color color;
            if( object.getColor() == null ) {
                color = c.getColors().getSelectedBlockColor();
            } else {
                color = object.getColor();
            }
            g.setColor( c.fadeOut(position, color) );

            for( int i = 0; i < object.getThickness(); i++ ) {
                g.drawRect( start.x() - i, start.y() - i, size.x() + 2 * i, size.y() + 2 * i );
            }
        }
    }
}
