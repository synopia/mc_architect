package de.funky_clan.mc.ui.renderer;

import de.funky_clan.mc.math.Position;
import de.funky_clan.mc.model.RenderContext;
import de.funky_clan.mc.model.SelectedBlock;

import java.awt.*;

/**
 * @author synopia
 */
public abstract class BaseRenderer<T> implements Renderer<T>{

    protected void renderBox( RenderContext c,
                              double worldStartX, double worldStartY, double worldStartZ,
                              double worldEndX, double worldEndY, double worldEndZ,
                              Color color, boolean fadeOut, boolean centered ) {
        Position position = c.getPosition();

        double sizeX = 0;
        double sizeY = 0;
        double sizeZ = 0;
        if( centered ) {
            sizeX = worldEndX-worldStartX;
            sizeY = worldEndY-worldStartY;
            sizeZ = worldEndZ-worldStartZ;
        }

        position.setWorld(worldEndX-sizeX/2, worldEndY-sizeY/2, worldEndZ-sizeZ/2 );
        int endX = position.getScreenX();
        int endY = position.getScreenY();

        position.setWorld(worldStartX-sizeX/2, worldStartY-sizeY/2, worldStartZ-sizeZ/2 );
        int startX = position.getScreenX();
        int startY = position.getScreenY();

        int x = startX<endX ? startX : endX;
        int y = startY<endY ? startY : endY;
        int w = Math.abs( endX-startX );
        int h = Math.abs( endY-startY );

        if( fadeOut ) {
            c.getGraphics().setColor( position.fadeOut( color ) );
        } else {
            c.getGraphics().setColor( color );
        }

        c.getGraphics().drawRect(x,y,w,h);
    }

}
