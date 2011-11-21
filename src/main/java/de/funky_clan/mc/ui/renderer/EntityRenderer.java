package de.funky_clan.mc.ui.renderer;

import de.funky_clan.mc.math.Position;
import de.funky_clan.mc.model.EntityBlock;
import de.funky_clan.mc.model.RenderContext;
import de.funky_clan.mc.model.SelectedBlock;

import java.awt.Graphics2D;

/**
 * @author synopia
 */
public class EntityRenderer extends BaseRenderer<EntityBlock> {
    public EntityRenderer() {
    }

    @Override
    public void render( EntityBlock object, RenderContext c ) {
        if( object.getImage()!=null ) {
            renderBox( c, object.getPositionX(), object.getPositionY(), object.getPositionZ(),
                    object.getPositionX() + object.getSizeX(), object.getPositionY() + object.getHeight(),
                    object.getPositionZ() + object.getSizeY(), object.getImage() );
        } else {
            renderBox( c, object.getPositionX(), object.getPositionY(), object.getPositionZ(),
                    object.getPositionX() + object.getSizeX(), object.getPositionY() + object.getHeight(),
                    object.getPositionZ() + object.getSizeY(), object.getColor(), true, false);
        }

        if( object.isDrawViewCone() ) {
            drawCone(object, c);
        }
    }

    private void drawCone(EntityBlock object, RenderContext c) {
        Position position = c.getPosition();
        position.setWorld( object.getPositionX(), object.getPositionY(), object.getPositionZ() );
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
