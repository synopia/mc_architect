package de.funky_clan.mc.model;

//~--- JDK imports ------------------------------------------------------------

import java.awt.*;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import javax.swing.*;
import javax.vecmath.Point2d;
import javax.vecmath.Point2i;

/**
 * @author synopia
 */
public class BackgroundImage implements Renderable {
    private String    filename;
    private ImageIcon icon;
    private Image     image;
    private Point2d start = new Point2d();
    private Point2d size  = new Point2d();

    public BackgroundImage(String filename, int startX, int startY) {
        this( filename, new Point2i(startX, startY) );
    }
    public BackgroundImage(String filename, Point2i start ) {
        this.start.set( start.x, start.y );
        this.filename = filename;

        try {
            image  = rotate(ImageIO.read( getClass().getClassLoader().getResourceAsStream( filename )), Math.toRadians(90));
            size.set( image.getWidth( null ), image.getHeight( null ) );
        } catch( IOException e ) {
            e.printStackTrace();
            image = null;
        }
    }

    @Override
    public void render( RenderContext c ) {
        if( image != null ) {
            Point2i screenSize = c.getScreenSize();
            int width  = screenSize.x;
            int height = screenSize.y;

            Point2i to = c.worldToScreen(start);
            to.negate();
            to.add(screenSize);

            Point2i from = c.worldToScreen( new Point2d(start.x + size.x-1, start.y + size.y) );
            from.negate();
            from.add(screenSize);
            int ex = to.x;
            int ey = to.y;
            int sx = from.x;
            int sy = from.y;

            int x1 = 0;
            int y1 = 0;
            int x2 = (int) size.x-1;
            int y2 = (int) size.y-1;

            if( ex>=0 && ey>=0 && sx<width && sy<height ) {
                Point2d startDiff = c.screenToWorld(new Point2i(sx, sy));
                startDiff.negate();
                startDiff.add( new Point2d(0,0) );

                Point2d endDiff = c.screenToWorld(new Point2i(screenSize.x, screenSize.y));
                endDiff.negate();
                endDiff.add( new Point2d(ex,ey));

                if( sx<0 ) {
                    x1 += Math.abs(startDiff.x);
                    sx = 0;
                }
                if( sy<0 ) {
                    y1 += Math.abs(startDiff.y);
                    sy = 0;
                }
                if( ex>=width ) {
                    x2 -= Math.abs(endDiff.x);
                    ex = width-1;
                }
                if( ey>=height ) {
                    y2 -= Math.abs(endDiff.y);
                    ey = height-1;
                }
                c.getGraphics().drawImage( image, sx, sy, ex, ey, x1, y1, x2, y2, null );
                c.getGraphics().setColor(Color.BLUE);
                c.getGraphics().drawRect( sx, sy, ex-sx, ey-sy);
            }

        }
    }

    public String getFilename() {
        return filename;
    }

    public Icon getIcon() {
        if( icon == null ) {
            int w;
            int h;

            if( size.x > size.y ) {
                w = 64;
                h = (int) ( 64.0 * size.y / size.x );
            } else {
                w = (int) ( 64.0 * size.x / size.y );
                h = 64;
            }

            icon = new ImageIcon( image.getScaledInstance( w, h, Image.SCALE_SMOOTH ));
        }

        return icon;
    }

    public static BufferedImage rotate(BufferedImage image, double angle) {
        double sin = Math.abs(Math.sin(angle)), cos = Math.abs(Math.cos(angle));
        int w = image.getWidth(), h = image.getHeight();
        int neww = (int)Math.floor(w*cos+h*sin), newh = (int)Math.floor(h*cos+w*sin);
        GraphicsConfiguration gc = getDefaultConfiguration();
        BufferedImage result = gc.createCompatibleImage(neww, newh, Transparency.TRANSLUCENT);
        Graphics2D g = result.createGraphics();
        g.translate((neww-w)/2, (newh-h)/2);
        g.rotate(angle, w/2, h/2);
        g.drawRenderedImage(image, null);
        g.dispose();
        return result;
    }
    public static GraphicsConfiguration getDefaultConfiguration() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        return gd.getDefaultConfiguration();

    }

}
