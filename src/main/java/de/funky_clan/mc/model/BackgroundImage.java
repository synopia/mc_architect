package de.funky_clan.mc.model;

//~--- JDK imports ------------------------------------------------------------

import java.awt.*;

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

    public BackgroundImage( String filename ) {
        this.filename = filename;

        try {
            image  = ImageIO.read( getClass().getClassLoader().getResourceAsStream( filename ));
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

            // todo is clipping rectangle applied to drawImage? is manual clipping required?
            c.getGraphics().drawImage( image, 0, 0, width, height, null );
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
}
