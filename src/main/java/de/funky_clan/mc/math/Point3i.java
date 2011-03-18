package de.funky_clan.mc.math;

/**
 * @author synopia
 */
public final class Point3i extends Point2i {
    private final int z;

    public Point3i(int x, int y, int z) {
        super(x, y);
        this.z = z;
    }

    public Point3i(Point3i point) {
        super(point);
        this.z = point.z;
    }

    public Point3i sub( Point3i v ) {
        return new Point3i(
                this.x - v.x,
                this.y - v.y,
                this.z - v.z
        );
    }

    public Point3i add( Point3i v ) {
        return new Point3i(
                this.x + v.x,
                this.y + v.y,
                this.z + v.z
        );
    }

    public int getZ() {
        return z;
    }
    public int z() {
        return z;
    }
}
