package de.funky_clan.mc.config;

//~--- non-JDK imports --------------------------------------------------------

import de.funky_clan.mc.model.*;

import de.funky_clan.mc.model.Graphics;
import org.jruby.embed.PathType;
import org.jruby.embed.ScriptingContainer;

//~--- JDK imports ------------------------------------------------------------

import java.awt.*;

import java.util.HashMap;

/**
 * @author synopia
 */
public class Configuration {
    private HashMap<String, BackgroundImage> images = new HashMap<String, BackgroundImage>();
    private Graphics                         graphics;
    private Model                            model;
    private int                              originX;
    private int                              originY;
    private int                              originSlice;
    private int midX;
    private int midY;
    private int midZ;

    public Configuration() {
        model    = new Model();
    }

    public Model getModel() {
        return model;
    }

    public HashMap<String, BackgroundImage> getImages() {
        return images;
    }

    public Colors createColors() {
        return new Colors();
    }

    public static Configuration createFromRuby( String filename ) {
        ScriptingContainer container     = new ScriptingContainer();
        Configuration      configuration = new Configuration();

        container.put( "@builder", configuration );
        container.runScriptlet( PathType.CLASSPATH, filename );

        return configuration;
    }

    public Configuration origin( int x, int y, int z, SliceType type ) {
        Slice slice = new Slice(model, type);
        graphics = new Graphics(slice);
        originX = x;
        originY = y;
        originSlice = z;

        int[] map = slice.mapSliceToWorld(x, y, z);
        midX = map[0];
        midY = map[1];
        midZ = map[2];

        return this;
    }

    public Configuration ellipse( int x, int y, int width, int height ) {
        for( int slice = 0; slice < 50; slice++ ) {
            ellipse( slice, x, y, width, height );
        }

        return this;
    }

    public Configuration ellipse( int slice, int x, int y, int width, int height ) {
        graphics.ellipse( slice+originSlice, x+originX, y+originY, width, height, 1 );

        return this;
    }

    public Configuration image( String filename ) {
        image( 0, 0, filename );

        return this;
    }

    public Configuration image( int sliceFrom, int sliceTo, String filename ) {
        for( int i = sliceFrom; i <= sliceTo; i++ ) {
            image( i, filename );
        }

        return this;
    }

    public Configuration image( int slice, String filename ) {
        BackgroundImage image = images.get( filename );

        if( image == null ) {
            image = new BackgroundImage( filename,originX, originY );
            images.put( filename, image );
        }

        model.addImage( SliceType.Z, slice+originSlice, image );

        return this;
    }

    public Configuration axis( int x, int y ) {
        graphics.hLine( 0, y, 50, 2 );
        graphics.vLine( x, 0, 50, 2 );

        return this;
    }

    public Configuration setPixel( int x, int y, int slice, int value ) {
        graphics.setPixel( x+originX, y+originY, slice+ originSlice, value );

        return this;
    }

    public int getOriginX() {
        return originX;
    }

    public int getOriginY() {
        return originY;
    }

    public int getOriginSlice() {
        return originSlice;
    }

    public int getMidX() {
        return midX;
    }

    public int getMidY() {
        return midY;
    }

    public int getMidZ() {
        return midZ;
    }

    public class Colors {
        public Color getBlockColor() {
            return Color.DARK_GRAY;
        }

        public Color getBackgroundColor() {
            return Color.LIGHT_GRAY;
        }

        public Color getPlayerBlockColor() {
            return Color.BLUE.brighter();
        }

        public Color getSelectedBlockColor() {
            return Color.BLUE;
        }
    }
}
