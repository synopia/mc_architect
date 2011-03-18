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

    public Point2d(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Point2d sub( Point2d v ) {
        return new Point2d(
                this.x - v.x,
                this.y - v.y
        );
    }

    public Point2d add( Point2d v ) {
        return new Point2d(
                this.x + v.x,
                this.y + v.y
        );
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

    public Point2d addScaled(double v, Point2d a) {
        return new Point2d(
                this.x + v * a.x,
                this.y + v * a.y
        );
    }
}
