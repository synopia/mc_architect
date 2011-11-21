package de.funky_clan.mc.services;

import com.google.inject.Singleton;
import de.funky_clan.mc.config.DataValues;
import de.funky_clan.mc.config.EntityType;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.IOException;
import java.util.HashMap;

/**
 * @author synopia
 */
@Singleton
public class ImageService {
    private final HashMap<DataValues, Tile> tiles = new HashMap<DataValues, Tile>();
    private final HashMap<EntityType, BufferedImage> images = new HashMap<EntityType, BufferedImage>();

    public ImageService() {
        addTile( DataValues.DIAMONDORE, 112, 48 );
        addTile( DataValues.COALORE, 112, 0 );
        addTile( DataValues.GOLDORE, 112, 32 );
        addTile( DataValues.IRONORE, 112, 16 );
        addTile( DataValues.REDSTONEORE, 128, 48 );
        addTile( DataValues.LAPIZLAZULIORE, 224, 128 );
        loadTiles("items.png");

        for (EntityType type : EntityType.values()) {
            if( type.getImageName()!=null ) {
                loadImage(type, type.getImageName());
            }
        }
    }

    public void addTile( DataValues value, int x, int y ) {
        tiles.put( value, new Tile( value, x, y ));
    }
    
    protected void loadImage( EntityType type, String name ) {
        try {
            BufferedImage image  = ImageIO.read( getClass().getResourceAsStream( "/" + name ));
            images.put(type, image);
        } catch (IOException e) {
            throw new IllegalStateException("Cannot load "+name);
        }
    }

    protected void loadTiles(String name) {
        try {
            int[]         pixels = new int[1 << 16];
            BufferedImage image  = ImageIO.read( getClass().getResourceAsStream( "/" + name ));

            for( Tile tile : tiles.values() ) {
                PixelGrabber grabber = new PixelGrabber( image, tile.x, tile.y, 16, 16, pixels, 0, 16 );

                grabber.grabPixels();

                BufferedImage tileImage = new BufferedImage( 16, 16, BufferedImage.TYPE_INT_ARGB );

                tileImage.setRGB( 0, 0, 16, 16, pixels, 0, 16 );
                tile.icon = new ImageIcon( tileImage );
            }
        } catch( IOException e ) {
            e.printStackTrace();    // To change body of catch statement use File | Settings | File Templates.
        } catch( InterruptedException e ) {
            e.printStackTrace();    // To change body of catch statement use File | Settings | File Templates.
        }
    }

    public BufferedImage getImage( EntityType type ) {
        return images.get(type);
    }

    public ImageIcon getIcon( DataValues type ) {
        if( tiles.containsKey( type )) {
            return tiles.get( type ).icon;
        }

        return null;
    }

    private static class Tile {
        public ImageIcon        icon;
        public final DataValues value;
        public final int        x;
        public final int        y;

        private Tile( DataValues value, int x, int y ) {
            this.value = value;
            this.x     = x;
            this.y     = y;
        }
    }
}
