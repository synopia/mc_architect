package de.funky_clan.mc.model;

//~--- JDK imports ------------------------------------------------------------

import java.awt.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import javax.swing.*;

/**
 * @author synopia
 */
public class BackgroundImage implements Renderable {
    private String    filename;
    private int       height;
    private ImageIcon icon;
    private Image     image;
    private int       width;
    private int midX;
    private int midY;

    public BackgroundImage(String filename, int midX, int midY) {
        this.midX = midX;
        this.midY = midY;
        this.filename = filename;

        try {
            image  = rotate(ImageIO.read( getClass().getClassLoader().getResourceAsStream( filename )), Math.toRadians(90));
            width  = image.getWidth( null );
            height = image.getHeight( null );

        } catch( IOException e ) {
            e.printStackTrace();
            image = null;
        }
    }

    @Override
    public void render( RenderContext c ) {
        if( image != null ) {
            int width  = c.getScreenWidth();
            int height = c.getScreenHeight();

            int ex = c.getScreenWidth()-c.modelToScreenX(midX - this.width/2);
            int ey = c.getScreenHeight()-c.modelToScreenY(midY - this.height/2);
            int sx = c.getScreenWidth()-c.modelToScreenX(midX + this.width/2);
            int sy = c.getScreenHeight()-c.modelToScreenY(midY + this.height/2);

            int x1 = 0;
            int y1 = 0;
            int x2 = this.width;
            int y2 = this.height;

            if( ex>=0 && ey>=0 && sx<width && sy<height ) {
                if( sx<0 ) {
                    x1 += Math.abs(c.screenToModelX(0)-c.screenToModelX(sx));
                    sx = 0;
                }
                if( sy<0 ) {
                    y1 += Math.abs(c.screenToModelY(0)-c.screenToModelY(sy));
                    sy = 0;
                }
                if( ex>=width ) {
                    x2 -= Math.abs(c.screenToModelX(ex)-c.screenToModelX(width));
                    ex = width-1;
                }
                if( ey>=height ) {
                    y2 -= Math.abs(c.screenToModelY(ey)-c.screenToModelY(height));
                    ey = height-1;
                }
                c.getGraphics().drawImage( image, sx, sy, ex, ey, x1, y1, x2, y2, null );
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

            if( width > height ) {
                w = 64;
                h = (int) ( 64.0 * height / width );
            } else {
                w = (int) ( 64.0 * width / height );
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
