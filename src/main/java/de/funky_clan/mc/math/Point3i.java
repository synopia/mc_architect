package de.funky_clan.mc.math;

/**
 * @author synopia
 */
public class Point3i extends Point2i {
    private int z;

    public Point3i(int x, int y, int z) {
        super(x, y);
        this.z = z;
    }

    public Point3i() {
    }

    public void set( Point3i v ) {
        set(v.x(), v.y(), v.z() );
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

    public void set( int x, int y, int z ) {
        super.set(x,y);
        this.z = z;
    }

    public int getZ() {
        return z;
    }
    public int z() {
        return z;
    }
}
