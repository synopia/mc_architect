package de.funky_clan.mc.ui.renderer;

import de.funky_clan.mc.math.Position;
import de.funky_clan.mc.model.Player;
import de.funky_clan.mc.model.RenderContext;

import java.awt.Graphics2D;

/**
 * @author synopia
 */
public class PlayerRenderer extends BlockRenderer<Player> {
    @Override
    public void render( Player object, RenderContext c ) {
        super.render( object, c );

        Position position = c.getPosition();

        position.setWorld( object.getPositionX(), object.getPositionY(), object.getPositionZ() );

        if( object.isDrawViewCone() ) {
            Graphics2D g         = c.getGraphics();
            int        sx        = position.getScreenX();
            int        sy        = position.getScreenY();
            int        w         = c.screenUnitX();
            int        direction = object.getDirection();
            int        x1        = sx + (int) ( 10 * w * Math.cos(( direction - 30 ) / 180.0 * Math.PI ));
            int        y1        = sy + (int) ( 10 * w * Math.sin(( direction - 30 ) / 180.0 * Math.PI ));
            int        x2        = sx + (int) ( 10 * w * Math.cos(( direction + 30 ) / 180.0 * Math.PI ));
            int        y2        = sy + (int) ( 10 * w * Math.sin(( direction + 30 ) / 180.0 * Math.PI ));

            g.drawLine( sx, sy, x1, y1 );
            g.drawLine( sx, sy, x2, y2 );
            g.drawLine( x1, y1, x2, y2 );
        }
    }
}
