package de.funky_clan.mc.math;

/**
 * @author synopia
 */
public final class Point3i extends Point2i {
    private final int z;

    public Point3i( Point3i point ) {
        super( point );
        this.z = point.z;
    }

    public Point3i( int x, int y, int z ) {
        super( x, y );
        this.z = z;
    }

    public Point3i sub( Point3i v ) {
        return new Point3i( this.x - v.x, this.y - v.y, this.z - v.z );
    }

    public Point3i add( Point3i v ) {
        return new Point3i( this.x + v.x, this.y + v.y, this.z + v.z );
    }

    public Point3d toPoint3d() {
        return new Point3d( this.x, this.y, this.z );
    }

    public int getZ() {
        return z;
    }

    public int z() {
        return z;
    }

    @Override
    public boolean equals( Object o ) {
        if( this == o ) {
            return true;
        }

        if(( o == null ) || ( getClass() != o.getClass() )) {
            return false;
        }

        if( !super.equals( o )) {
            return false;
        }

        Point3i point3i = (Point3i) o;

        if( z != point3i.z ) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();

        result = 31 * result + z;

        return result;
    }

    @Override
    public String toString() {
        return "Point3i{" + "x=" + x + "y=" + y + "z=" + z + '}';
    }
}
