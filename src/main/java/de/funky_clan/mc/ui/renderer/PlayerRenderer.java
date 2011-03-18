package de.funky_clan.mc.ui.renderer;

import de.funky_clan.mc.math.Point2i;
import de.funky_clan.mc.math.Point3d;
import de.funky_clan.mc.model.Player;
import de.funky_clan.mc.model.SelectedBlock;
import de.funky_clan.mc.model.SliceRenderContext;

import java.awt.*;

/**
 * @author synopia
 */
public class PlayerRenderer extends BlockRenderer<Player> implements Renderer<Player, SliceRenderContext>{
    @Override
    public void render(Player object, SliceRenderContext c) {
        super.render( object, c );

        Point3d position = object.getPosition();
        if( position!=null && object.isDrawViewCone() ) {
            Graphics2D g = c.getGraphics();

            Point2i start = c.worldToScreen(position);
            Point2i size = c.screenUnit(position);

            int sx = start.x();
            int sy = start.y();
            int w  = size.x();
            int h  = size.y();
            int mx = sx + w / 2;
            int my = sy + h / 2;
            int direction = object.getDirection();
            int x1 = mx + (int) ( 10 * w * Math.cos(( direction - 30 ) / 180.0 * Math.PI ));
            int y1 = my + (int) ( 10 * w * Math.sin(( direction - 30 ) / 180.0 * Math.PI ));
            int x2 = mx + (int) ( 10 * w * Math.cos(( direction + 30 ) / 180.0 * Math.PI ));
            int y2 = my + (int) ( 10 * w * Math.sin(( direction + 30 ) / 180.0 * Math.PI ));

            g.drawLine( mx, my, x1, y1 );
            g.drawLine( mx, my, x2, y2 );
            g.drawLine( x1, y1, x2, y2 );
        }

    }
}
