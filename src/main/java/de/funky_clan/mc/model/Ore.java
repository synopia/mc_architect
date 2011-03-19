package de.funky_clan.mc.model;

import de.funky_clan.mc.math.Point3i;
import de.funky_clan.mc.ui.renderer.Renderer;

/**
 * @author synopia
 */
public class Ore {
    private Point3i start;
    private Point3i end;

    public Ore(Point3i start) {
        this( start, start );
    }

    public Ore(Point3i start, Point3i end) {
        this.start = start;
        this.end = end;
    }

    public Point3i getStart() {
        return start;
    }

    public Point3i getEnd() {
        return end;
    }

    public void addOre( Point3i pos  ) {
        start = new Point3i(
                Math.min( start.x(), pos.x() ),
                Math.min( start.y(), pos.y() ),
                Math.min( start.z(), pos.z() )
        );
        end = new Point3i(
                Math.max( end.x(), pos.x() ),
                Math.max( end.y(), pos.y() ),
                Math.max( end.z(), pos.z() )
        );
    }

    public boolean contains(Point3i pos) {
        return pos.x()>=start.x() && pos.y()>=start.y() && pos.z()>=start.z() && pos.x()<=end.x() && pos.y()<=end.y() && pos.z()<=end.z();
    }

    public void addOre( Ore ore ) {
        addOre( ore.getStart() );
        addOre( ore.getEnd() );
    }

    public void render(RenderContext c) {
        // draw selected block with player color
        //Point2d startScreen = c.worldToScreen( startWorld );
        //Point2d endScreen   = c.worldToScreen( endWorld );

        //..
    }
}
