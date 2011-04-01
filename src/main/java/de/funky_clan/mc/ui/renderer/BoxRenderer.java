package de.funky_clan.mc.ui.renderer;

import de.funky_clan.mc.model.Box;
import de.funky_clan.mc.model.RenderContext;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * @author synopia
 */
public class BoxRenderer extends BaseRenderer<Box> {
    @Override
    public void render( Box object, RenderContext c ) {
        Graphics2D g     = c.getGraphics();
        Color      color = c.getColors().getSelectedBlockColor();

        renderBox( c, object.getStartX(), object.getStartY(), object.getStartZ(), object.getEndX(), object.getEndY(),
                   object.getEndZ(), color, true, false, false );
    }
}
