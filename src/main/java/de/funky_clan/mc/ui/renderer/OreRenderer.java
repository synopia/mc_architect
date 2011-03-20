package de.funky_clan.mc.ui.renderer;

import com.google.inject.Inject;
import de.funky_clan.mc.config.Colors;
import de.funky_clan.mc.math.Position;
import de.funky_clan.mc.model.Ore;
import de.funky_clan.mc.model.RenderContext;

import java.util.List;

/**
 * @author synopia
 */
public class OreRenderer implements Renderer<List<Ore>>{
    @Inject
    private Colors colors;


    @Override
    public void render(List<Ore> ores, RenderContext c) {
        Position position = c.getPosition();
        c.updateWindowBox();
        for (Ore ore : ores) {
            if( c.contains(ore.getStartX(), ore.getStartY(), ore.getEndZ()) ||
                    c.contains(ore.getEndX(), ore.getStartY(), ore.getEndZ() ) ) {
                position.setWorld(ore.getEndX(),   ore.getEndY(),   ore.getEndZ() );
                int endX = position.getScreenX();
                int endY = position.getScreenY();

                position.setWorld(ore.getStartX(), ore.getStartY(), ore.getStartZ() );
                int startX = position.getScreenX();
                int startY = position.getScreenY();

                int x = startX<endX ? startX : endX;
                int y = startY<endY ? startY : endY;
                int w = Math.abs( endX-startX );
                int h = Math.abs( endY-startY );
                w += c.screenUnitX(1);
                h += c.screenUnitY(1);

                if(Math.abs(position.getSliceNo() - c.getCurrentSlice())<20) {
                    c.getGraphics().setColor( position.fadeOut( colors.getSelectedBlockColor()) );
                    c.getGraphics().drawRect(x,y,w,h);
                }
            }
        }
    }
}
