package de.funky_clan.mc.math;

/**
 * @author synopia
 */
public class Point2i {
    protected final int x;
    protected final int y;

    public Point2i(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point2i(Point2i point) {
        this( point.x, point.y );
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

    public Point2d toPoint2d() {
        return new Point2d(x,y);
    }
}
