package de.funky_clan.mc.scripts;

/**
 * Contains methods to "draw" into the model
 *
 * @author synopia
 */
public abstract class Graphics {
    private double scaleX = 1;
    private double scaleY = 1;
    private double scaleZ = 1;
    private double originX;
    private double originY;
    private double originZ;

    public void setOrigin( double x, double y, double z ) {
        originX = x;
        originY = y;
        originZ = z;
    }

    public void setScale( double scaleX, double scaleY, double scaleZ ) {
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.scaleZ = scaleZ;
    }

    public void ellipse( double xm, double ym, double z, double width, double height, int value ) {
        int a   = (int) width;
        int b   = (int) height;
        int dx  = 0;
        int dy  = b;
        int a2  = a * a;
        int b2  = b * b;
        int err = b2 - ( 2 * b - 1 ) * a2;
        int e2;

        do {
            setPixel( xm + dx, ym + dy, z, value );
            setPixel( xm - dx, ym + dy, z, value );
            setPixel( xm - dx, ym - dy, z, value );
            setPixel( xm + dx, ym - dy, z, value );
            e2 = 2 * err;

            if( e2 < ( 2 * dx + 1 ) * b2 ) {
                dx++;
                err += ( 2 * dx + 1 ) * b2;
            }

            if( e2 > -( 2 * dy - 1 ) * a2 ) {
                dy--;
                err -= ( 2 * dy - 1 ) * a2;
            }
        } while( dy >= 0 );

        while( dx + 1 < a ) {
            dx++;
            setPixel( xm + dx, ym, z, value );
            setPixel( xm - dx, ym, z, value );
        }
    }

    public void setPixel( double x, double y, double z, int value ) {
        setPixelLocal( x * scaleX + originX, y * scaleY + originY, z * scaleZ + originZ, value );
    }

    public int getPixel( double x, double y, double z ) {
        return getPixelLocal( x * scaleX + originX, y * scaleY + originY, z * scaleZ + originZ );
    }

    public abstract void setPixelLocal( double x, double y, double z, int value );

    public abstract int getPixelLocal( double x, double y, double z );
}
