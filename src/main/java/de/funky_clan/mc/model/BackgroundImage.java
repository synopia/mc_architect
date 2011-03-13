package de.funky_clan.mc.model;

//~--- JDK imports ------------------------------------------------------------

import java.awt.*;

import java.awt.image.BufferedImage;
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
    private int startX;
    private int startY;

    public BackgroundImage(String filename, int startX, int startY) {
        this.startX = startX;
        this.startY = startY;
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

            int ex = c.getScreenWidth()-c.modelToScreenX(startX);
            int ey = c.getScreenHeight()-c.modelToScreenY(startY);
            int sx = c.getScreenWidth()-c.modelToScreenX(startX + this.width-1);
            int sy = c.getScreenHeight()-c.modelToScreenY(startY + this.height);

            ex += c.screenUnitX(startX)/2;
            ey += c.screenUnitY(startY)/2;
            sx -= c.screenUnitX(startX)/2;
            sy -= c.screenUnitY(startY)/2;

            int x1 = 0;
            int y1 = 0;
            int x2 = this.width-1;
            int y2 = this.height-1;

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
