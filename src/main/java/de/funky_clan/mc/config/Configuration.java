package de.funky_clan.mc.config;

//~--- non-JDK imports --------------------------------------------------------

import com.google.inject.Inject;
import com.google.inject.Provider;
import de.funky_clan.mc.math.Point3i;
import de.funky_clan.mc.model.*;

import de.funky_clan.mc.model.Graphics;
import org.jruby.embed.PathType;
import org.jruby.embed.ScriptingContainer;

//~--- JDK imports ------------------------------------------------------------

import java.util.HashMap;

/**
 * @author synopia
 */
public class Configuration {
    private HashMap<String, BackgroundImage> images = new HashMap<String, BackgroundImage>();
    private Graphics                         graphics;
    @Inject
    private Model                            model;
    private int                              originX;
    private int                              originY;
    private int                              originSlice;
    private int midX;
    private int midY;
    private int midZ;
    private Slice slice;

    @Inject
    private Provider<Slice> sliceProvider;

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
        slice = sliceProvider.get();
        slice.setType( type );
        graphics = new Graphics(slice);
        originX = x;
        originY = y;
        originSlice = z;

        Point3i map = slice.sliceToWorld(new Point3i(x, y, z));
        midX = map.x();
        midY = map.y();
        midZ = map.z();

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

    public Configuration image( int x, int y,String filename ) {
        image(0, 0, x, y, filename);

        return this;
    }

    public Configuration image( int sliceFrom, int sliceTo, int x, int y,String filename ) {
        for( int i = sliceFrom; i <= sliceTo; i++ ) {
            image( i, x, y,filename );
        }

        return this;
    }

    public Configuration image( int slice, int x, int y, String filename ) {
        BackgroundImage image = images.get( filename );

        if( image == null ) {
            image = new BackgroundImage( filename,x, y );
            images.put( filename, image );
        }

        model.addImage( SliceType.Z, slice+originSlice, image );

        return this;
    }

    public Configuration axis( int x, int y ) {
        graphics.hLine( 0, y, 50, 2 );
        graphics.vLine(x, 0, 50, 2);

        return this;
    }

    public Configuration setPixel( int x, int y, int slice, int value ) {
        graphics.setPixel(x + originX, y + originY, slice + originSlice, value);

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

}


