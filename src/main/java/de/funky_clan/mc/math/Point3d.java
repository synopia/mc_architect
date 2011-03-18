package de.funky_clan.mc.math;

/**
 * @author synopia
 */
public class Point3d extends Point2d {
    private double z;

    public Point3d(double x, double y, double z) {
        super(x, y);
        this.z = z;
    }

    public Point3d() {
    }

    public void set( Point3d v ) {
        set(v.x(), v.y(), v.z() );
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


    public void set( double x, double y, double z ) {
        super.set(x,y);
        this.z = z;
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

}
