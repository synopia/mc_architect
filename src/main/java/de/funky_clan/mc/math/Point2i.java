package de.funky_clan.mc.math;

import java.awt.Point;

/**
 * @author synopia
 */
public class Point2i {
    protected final int x;
    protected final int y;

    public Point2i( Point p ) {
        this( p.x, p.y );
    }

    public Point2i( Point2i point ) {
        this( point.x, point.y );
    }

    public Point2i( int x, int y ) {
        this.x = x;
        this.y = y;
    }

    public Point2i sub( Point2i v ) {
        return new Point2i( this.x - v.x, this.y - v.y );
    }

    public Point2i add( Point2i v ) {
        return new Point2i( this.x + v.x, this.y + v.y );
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    public Point2d toPoint2d() {
        return new Point2d( x, y );
    }

    @Override
    public boolean equals( Object o ) {
        if( this == o ) {
            return true;
        }

        if(( o == null ) || ( getClass() != o.getClass() )) {
            return false;
        }

        Point2i point2i = (Point2i) o;

        if( x != point2i.x ) {
            return false;
        }

        if( y != point2i.y ) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = x;

        result = 31 * result + y;

        return result;
    }

    @Override
    public String toString() {
        return "Point2i{" + "x=" + x + ", y=" + y + '}';
    }
}
