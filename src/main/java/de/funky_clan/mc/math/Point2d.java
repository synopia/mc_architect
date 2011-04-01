package de.funky_clan.mc.math;

/**
 * @author synopia
 */
public class Point2d {
    protected final double x;
    protected final double y;

    public Point2d( Point2d point ) {
        this( point.x, point.y );
    }

    public Point2d( double x, double y ) {
        this.x = x;
        this.y = y;
    }

    public Point2d sub( Point2d v ) {
        return new Point2d( this.x - v.x, this.y - v.y );
    }

    public Point2d add( Point2d v ) {
        return new Point2d( this.x + v.x, this.y + v.y );
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double x() {
        return x;
    }

    public double y() {
        return y;
    }

    public Point2d scale( double a, double b ) {
        return new Point2d( this.x * a, this.y * b );
    }

    public Point2d addScaled( double v, Point2d a ) {
        return new Point2d( this.x + v * a.x, this.y + v * a.y );
    }

    @Override
    public boolean equals( Object o ) {
        if( this == o ) {
            return true;
        }

        if(( o == null ) || ( getClass() != o.getClass() )) {
            return false;
        }

        Point2d point2d = (Point2d) o;

        if( Double.compare( point2d.x, x ) != 0 ) {
            return false;
        }

        return Double.compare( point2d.y, y ) == 0;
    }

    @Override
    public int hashCode() {
        int  result;
        long temp;

        temp   = ( x != +0.0d )
                 ? Double.doubleToLongBits( x )
                 : 0L;
        result = (int) ( temp ^ ( temp >>> 32 ));
        temp   = ( y != +0.0d )
                 ? Double.doubleToLongBits( y )
                 : 0L;
        result = 31 * result + (int) ( temp ^ ( temp >>> 32 ));

        return result;
    }

    @Override
    public String toString() {
        return "Point2d{" + "x=" + x + ", y=" + y + '}';
    }
}
