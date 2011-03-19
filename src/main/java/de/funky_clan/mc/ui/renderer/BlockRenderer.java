package de.funky_clan.mc.ui.renderer;

import de.funky_clan.mc.math.Position;
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
        Position position = c.getPosition();
        position.setWorld(object.getPositionX(), object.getPositionY(), object.getPositionZ());

        int startX = position.getScreenX();
        int startY = position.getScreenY();
        int sizeX = c.screenUnitX(1);
        int sizeY = c.screenUnitY(1);
        if( object.getType()==SelectedBlock.Type.CENTERED ) {
            startX -= sizeX/2;
            startY -= sizeY/2;
        }

        Color color;
        if( object.getColor() == null ) {
            color = c.getColors().getSelectedBlockColor();
        } else {
            color = object.getColor();
        }
        g.setColor(position.fadeOut(color));

        for( int i = 0; i < object.getThickness(); i++ ) {
            g.drawRect( startX - i, startY - i, sizeX + 2 * i, sizeY + 2 * i );
        }
    }
}
