package de.funky_clan.mc.ui.renderer;

import com.google.inject.Inject;
import de.funky_clan.mc.config.Colors;
import de.funky_clan.mc.math.Position;
import de.funky_clan.mc.model.Ore;
import de.funky_clan.mc.model.RenderContext;

import java.awt.*;
import java.util.List;

/**
 * @author synopia
 */
public class OreRenderer extends BaseRenderer<List<Ore>>{
    @Inject
    private Colors colors;


    @Override
    public void render(List<Ore> ores, RenderContext c) {
        c.updateWindowBox();
        for (Ore ore : ores) {
            if( c.contains(ore.getStartX(), ore.getStartY(), ore.getEndZ()) ||
                    c.contains(ore.getEndX(), ore.getStartY(), ore.getEndZ() ) ) {
                renderBox(c,
                        ore.getStartX(), ore.getStartY(), ore.getStartZ(),
                        ore.getEndX()+1, ore.getEndY()+1, ore.getEndZ()+1,
                        colors.getSelectedBlockColor(), true, false );
            }
        }
    }
}
