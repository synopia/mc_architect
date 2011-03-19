package de.funky_clan.mc.model;

//~--- JDK imports ------------------------------------------------------------

import de.funky_clan.mc.math.Point2d;
import de.funky_clan.mc.math.Point2i;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @author synopia
 */
public class BackgroundImage {
    private String    filename;
    private ImageIcon icon;
    private Image     image;
    private Point2d start;
    private Point2d size;

    public BackgroundImage(String filename, int startX, int startY) {
        this( filename, new Point2i(startX, startY) );
    }
    public BackgroundImage(String filename, Point2i start ) {
        this.start = start.toPoint2d();
        this.filename = filename;

        try {
            image  = rotate(ImageIO.read( getClass().getClassLoader().getResourceAsStream( filename )), Math.toRadians(90));
            size = new Point2d( image.getWidth( null ), image.getHeight( null ) );
        } catch( IOException e ) {
            e.printStackTrace();
            image = null;
        }
    }

    public String getFilename() {
        return filename;
    }

    public Image getImage() {
        return image;
    }

    public Point2d getStart() {
        return start;
    }

    public Point2d getSize() {
        return size;
    }

    public Icon getIcon() {
        if( icon == null ) {
            int w;
            int h;

            if( size.x() > size.y() ) {
                w = 64;
                h = (int) ( 64.0 * size.y() / size.x() );
            } else {
                w = (int) ( 64.0 * size.x() / size.y() );
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
