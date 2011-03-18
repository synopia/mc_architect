package de.funky_clan.mc.model;

//~--- JDK imports ------------------------------------------------------------

import java.awt.*;

import javax.swing.*;
import javax.vecmath.Point2i;

/**
 * @author paul.fritsche@googlemail.com
 */
public class Player extends SelectedBlock {
    private int     direction;
    private boolean drawViewCone;
    private int     z;

    public Player( boolean drawViewCone ) {
        this.drawViewCone = drawViewCone;
    }

    public boolean isDrawViewCone() {
        return drawViewCone;
    }

    public int getZ() {
        return z;
    }

    public void setZ( int z ) {
        this.z = z;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection( int direction ) {
        this.direction = direction+90;
    }

    @Override
    public void render( RenderContext c ) {

        // draw selected block with player color
        Color color = c.getColors().getPlayerBlockColor();

        setColor( color );
        super.render( c );

        if( isDrawViewCone() ) {
            Graphics2D g = c.getGraphics();

            g.setColor( color );

            Point2i start = c.worldToScreen(getPosition());
            Point2i size = c.screenUnit(getPosition());

            int sx = start.x;
            int sy = start.y;
            int w  = size.x;
            int h  = size.y;
            int mx = sx - w / 2;
            int my = sy - h / 2;
            int x1 = mx + (int) ( 10 * w * Math.cos(( direction - 30 ) / 180.0 * Math.PI ));
            int y1 = my + (int) ( 10 * w * Math.sin(( direction - 30 ) / 180.0 * Math.PI ));
            int x2 = mx + (int) ( 10 * w * Math.cos(( direction + 30 ) / 180.0 * Math.PI ));
            int y2 = my + (int) ( 10 * w * Math.sin(( direction + 30 ) / 180.0 * Math.PI ));

            g.drawLine( c.getScreenSize().x-mx, c.getScreenSize().y-my, c.getScreenSize().x-x1, c.getScreenSize().y-y1 );
            g.drawLine( c.getScreenSize().x-mx, c.getScreenSize().y-my, c.getScreenSize().x-x2, c.getScreenSize().y-y2 );
            g.drawLine( c.getScreenSize().x-x1, c.getScreenSize().y-y1, c.getScreenSize().x-x2, c.getScreenSize().y-y2 );
        }
    }

/*
    todo verify
    public void repaint( JComponent component, RenderContext c ) {
        component.repaint(
                c.modelToScreenX(getX() - 15), c.modelToScreenX(getY() - 15),
                c.screenUnitX(getX()-15, getX()+15), c.screenUnitY(getY()-15, getY()+15)
        );
    }
*/
}
