package de.funky_clan.mc.math;

/**
 * @author synopia
 */
public class Point2i {
    protected int x;
    protected int y;

    public Point2i(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point2i() {
    }

    public void set( Point2i v ) {
        set(v.x(), v.y());
    }
    public void set( int x, int y ) {
        this.x = x;
        this.y = y;
    }

    public Point2i sub( Point2i v ) {
        return new Point2i(
                this.x - v.x,
                this.y - v.y
        );
    }

    public Point2i add( Point2i v ) {
        return new Point2i(
                this.x + v.x,
                this.y + v.y
        );
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
}
