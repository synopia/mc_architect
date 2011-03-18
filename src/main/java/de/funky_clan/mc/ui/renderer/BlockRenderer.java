package de.funky_clan.mc.ui.renderer;

import de.funky_clan.mc.math.Point2i;
import de.funky_clan.mc.math.Point3d;
import de.funky_clan.mc.model.SelectedBlock;
import de.funky_clan.mc.model.SliceRenderContext;

import java.awt.*;

/**
 * @author synopia
 */
public class BlockRenderer<B extends SelectedBlock> implements Renderer<B, SliceRenderContext> {
    @Override
    public void render(B object, SliceRenderContext c) {
        Graphics2D g  = c.getGraphics();

        Point3d position = object.getPosition();
        if( position!=null ) {
            Point2i start = c.worldToScreen(position);
            Point2i size  = c.screenUnit(position);

            if( object.getColor() == null ) {
                g.setColor( c.getColors().getSelectedBlockColor() );
            } else {
                g.setColor( object.getColor() );
            }

            for( int i = 0; i < object.getThickness(); i++ ) {
                g.drawRect( start.x() - i, start.y() - i, size.x() + 2 * i, size.y() + 2 * i );
            }
        }
    }
}
