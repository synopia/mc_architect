package de.funky_clan.mc.model;

import de.funky_clan.mc.math.Point2d;
import de.funky_clan.mc.math.Point2i;
import de.funky_clan.mc.math.Point3d;

/**
 * @author synopia
 */
public class SliceRenderContext extends RenderContext {
    private int currentSlice;
    private Slice slice;

    public SliceRenderContext(Slice slice) {
        this.slice = slice;
    }

    @Override
    public Point3d screenToWorld(Point2i screenPos) {
        Point2d slicePos = super.screenToWorld(screenPos);
        return slice.sliceToWorld(new Point3d(slicePos.x(), slicePos.y(), currentSlice));
    }

    public Point2i worldToScreen(Point3d worldPos) {
        Point3d slicePos = slice.worldToSlice(worldPos);
        return worldToScreen(new Point2d( slicePos.x(), slicePos.y()));
    }
}
