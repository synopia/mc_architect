package de.funky_clan.mc.ui.renderer;

import com.google.inject.Inject;
import de.funky_clan.mc.config.Colors;
import de.funky_clan.mc.math.Point2i;
import de.funky_clan.mc.math.Point3d;
import de.funky_clan.mc.math.Point3i;
import de.funky_clan.mc.model.Ore;
import de.funky_clan.mc.model.OreDetector;
import de.funky_clan.mc.model.RenderContext;

import java.util.List;

/**
 * @author synopia
 */
public class OreRenderer implements Renderer<List<Ore>>{
    @Inject
    Colors colors;


    @Override
    public void render(List<Ore> ores, RenderContext c) {
        for (Ore ore : ores) {
            Point3d slicePos = c.worldToSlice(ore.getStart().toPoint3d());
            Point2i start = c.sliceToScreen(slicePos);
            Point2i end   = c.worldToScreen(ore.getEnd().toPoint3d());
            int x = start.x()<end.x() ? start.x() : end.x();
            int y = start.y()<end.y() ? start.y() : end.y();
            int w = Math.abs( end.x()-start.x() );
            int h = Math.abs( end.y()-start.y() );
            w += c.screenUnit(slicePos).x();
            h += c.screenUnit(slicePos).y();

            Point3i size = ore.getEnd().sub(ore.getStart());
            if(Math.abs(slicePos.z()-c.getCurrentSlice())<20) {
                c.getGraphics().setColor( c.fadeOut( ore.getStart().toPoint3d(), colors.getSelectedBlockColor()) );
                c.getGraphics().drawRect(x,y,w,h);
            }
        }
    }
}
