package de.funky_clan.mc.math;

/**
 * @author synopia
 */
public final class Point3d extends Point2d {
    private final double z;
    public static Point3d NULL = new Point3d(0,0,0);

    public Point3d(double x, double y, double z) {
        super(x, y);
        this.z = z;
    }

    public Point3d(Point3d point) {
        super(point);
        this.z = point.z;
    }

    public Point3d sub( Point3d v ) {
        return new Point3d(
                this.x - v.x,
                this.y - v.y,
                this.z - v.z
        );
    }

    public Point3d add( Point3d v ) {
        return new Point3d(
                this.x + v.x,
                this.y + v.y,
                this.z + v.z
        );
    }


    public double getZ() {
        return z;
    }

    public double z() {
        return z;
    }

    public Point3d addScaled(double v, Point3d a) {
        return new Point3d(
                this.x + v * a.x,
                this.y + v * a.y,
                this.z + v * a.z
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Point3d point3d = (Point3d) o;

        if (Double.compare(point3d.z, z) != 0) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        long temp;
        temp = z != +0.0d ? Double.doubleToLongBits(z) : 0L;
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "Point3d{" +
                "x=" + x +
                "y=" + y +
                "z=" + z +
                '}';
    }
}
