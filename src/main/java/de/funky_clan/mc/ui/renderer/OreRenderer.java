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
public class OreRenderer extends BaseRenderer<List<Ore>> {
    @Inject
    private Colors colors;

    @Override
    public void render( List<Ore> ores, RenderContext c ) {
        c.updateWindowBox();

        for( Ore ore : ores ) {
            Position position = c.getPosition();
            position.setWorld( ore.getStartX(), ore.getStartY(), ore.getStartZ() );
            float alpha = 1;
            double distToSlice = position.distToSlice();
            if(distToSlice!=0) {
                alpha = 1.f / (float) Math.abs(distToSlice);
            }
            char ch = 0;
            if( distToSlice>0 ) {
                ch = '^';
            } else if( distToSlice <0 ) {
                ch = 'v';
            } else {
                ch = '-';
            }

            renderBox( c, ore.getStartX(), ore.getStartY(), ore.getStartZ(), ore.getEndX(), ore.getEndY(),
                       ore.getEndZ(), ore.getColor(), alpha, ch+"", false, true, true );
        }
    }
}
