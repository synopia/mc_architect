package de.funky_clan.mc.config;

import de.funky_clan.mc.model.BackgroundImage;
import de.funky_clan.mc.model.Graphics;
import de.funky_clan.mc.model.Model;
import de.funky_clan.mc.model.Slice;
import org.jruby.embed.PathType;
import org.jruby.embed.ScriptingContainer;

import java.awt.*;
import java.util.HashMap;

/**
 * @author synopia
 */
public class Configuration {
    private Model    model;
    private Graphics graphics;
    private HashMap<String, BackgroundImage> images = new HashMap<String, BackgroundImage>();
    private int originX;
    private int originY;
    private int originZ;

    public Model getModel() {
        return model;
    }

    public Configuration() {

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

    public Colors createColors() {
        return new Colors();
    }

    public static Configuration createFromRuby( String filename ) {
        ScriptingContainer container = new ScriptingContainer();
        Configuration configuration = new Configuration();
        container.put("@builder", configuration);
        container.runScriptlet(PathType.CLASSPATH, filename);
        return configuration;
    }

    public Configuration origin(int x, int y, int z) {
        originX = x;
        originY = y;
        originZ = z;

        return this;
    }

    public Configuration create(int width, int height, int slices) {
        model = new Model(width, height, slices);
        graphics = new Graphics(model);

        return this;
    }

    public Configuration ellipse(int x, int y, int width, int height) {
        for (int slice = 0; slice < model.getSizeZ(); slice++) {
            ellipse(slice, x, y, width, height);
        }

        return this;
    }

    public Configuration ellipse(int slice, int x, int y, int width, int height) {
        graphics.ellipse(slice, x, y, width, height, 1);

        return this;
    }

    public Configuration image(String filename) {
        image(0, model.getSizeZ(), filename);

        return this;
    }

    public Configuration image(int sliceFrom, int sliceTo, String filename) {
        for (int i = sliceFrom; i <= sliceTo; i++) {
            image(i, filename);
        }

        return this;
    }

    public Configuration image(int slice, String filename) {
        BackgroundImage image = images.get(filename);
        if (image == null) {
            image = new BackgroundImage(filename);
            images.put(filename, image);
        }
        model.addImage(Slice.SliceType.Z, slice, image);

        return this;
    }

    public Configuration axis(int x, int y) {
        graphics.hLine(0, y, model.getSizeX(), 2);
        graphics.vLine(x, 0, model.getSizeY(), 2);

        return this;
    }

    public Configuration setPixel( int x, int y, int z, int value ) {
        graphics.setPixel( x, y, z, value);

        return this;
    }

    public int getOriginX() {
        return originX;
    }

    public int getOriginY() {
        return originY;
    }

    public int getOriginZ() {
        return originZ;
    }
}
