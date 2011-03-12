package de.funky_clan.mc.model;

/**
 * Contains methods to "draw" into the model
 *
 * @author synopia
 */
public class Graphics {
    private Slice slice;

    public Graphics( Slice slice ) {
        this.slice = slice;
    }

    public void ellipse( int level, int xm, int ym, int width, int height, int type ) {
        int a   = width;
        int b   = height;
        int dx  = 0;
        int dy  = b;
        int a2  = a * a;
        int b2  = b * b;
        int err = b2 - ( 2 * b - 1 ) * a2;
        int e2  = 0;

        do {
            setPixel( xm + dx, ym + dy, level, type );
            setPixel( xm - dx, ym + dy, level, type );
            setPixel( xm - dx, ym - dy, level, type );
            setPixel( xm + dx, ym - dy, level, type );
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
            setPixel( xm + dx, ym, level, type );
            setPixel( xm - dx, ym, level, type );
        }
    }

    public void setPixel( int x, int y, int slice, int value ) {
        this.slice.setPixel(x, y, slice, value);
    }

    public void hLine( int x, int y, int width, int level, int type ) {
        for( int i = 0; i < width; i++ ) {
            setPixel( x + i, y, level, type );
        }
    }

    public void vLine( int x, int y, int height, int level, int type ) {
        for( int i = 0; i < height; i++ ) {
            setPixel( x, y + i, level, type );
        }
    }

    public void hLine( int x, int y, int width, int type ) {
        for( int level = 0; level < 50; level++ ) {
            hLine( x, y, width, level, type );
        }
    }

    public void vLine( int x, int y, int height, int type ) {
        for( int level = 0; level < 50; level++ ) {
            vLine( x, y, height, level, type );
        }
    }
}
