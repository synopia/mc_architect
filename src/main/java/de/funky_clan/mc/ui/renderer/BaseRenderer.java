package de.funky_clan.mc.ui.renderer;

import de.funky_clan.mc.math.Position;
import de.funky_clan.mc.model.RenderContext;

import java.awt.Color;

/**
 * @author synopia
 */
public abstract class BaseRenderer<T> implements Renderer<T> {
    protected void renderBox( RenderContext c, double worldStartX, double worldStartY, double worldStartZ,
                              double worldEndX, double worldEndY, double worldEndZ, Color color, boolean centered,
                              boolean unitSize ) {
        renderBox(c, worldStartX, worldStartY, worldStartZ, worldEndX, worldEndY, worldEndZ, color, centered, unitSize, false);
    }
    protected void renderBox( RenderContext c, double worldStartX, double worldStartY, double worldStartZ,
                              double worldEndX, double worldEndY, double worldEndZ, Color color, boolean centered,
                              boolean unitSize, boolean solid ) {
        Position position = c.getPosition();
        double   sizeX    = 0;
        double   sizeZ    = 0;

        if( centered ) {
            sizeX = worldEndX - worldStartX;
            sizeZ = worldEndZ - worldStartZ;
        }

        position.setWorld( worldEndX - sizeX / 2, worldEndY, worldEndZ - sizeZ / 2 );

        int endX = position.getScreenX();
        int endY = position.getScreenY();

        position.setWorld( worldStartX - sizeX / 2, worldStartY, worldStartZ - sizeZ / 2 );

        int startX = position.getScreenX();
        int startY = position.getScreenY();
        int x      = ( startX < endX )
                     ? startX
                     : endX;
        int y      = ( startY < endY )
                     ? startY
                     : endY;
        int w      = Math.abs( endX - startX );
        int h      = Math.abs( endY - startY );

        if( unitSize ) {
            w += c.screenUnitX();
            h += c.screenUnitY();
        }

        if( solid ) {
            c.getGraphics().setColor( position.fadeOut( color ));
            c.getGraphics().fillRect( x, y, w, h );
            c.getGraphics().setColor( color );
            c.getGraphics().drawRect( x, y, w, h );
        } else {
            c.getGraphics().setColor( position.fadeOut( color ));
            c.getGraphics().drawRect( x, y, w, h );
        }
    }
}
