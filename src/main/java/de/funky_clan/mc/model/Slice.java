package de.funky_clan.mc.model;

//~--- JDK imports ------------------------------------------------------------

import com.google.inject.Inject;
import de.funky_clan.mc.config.DataValues;
import de.funky_clan.mc.math.Point2d;
import de.funky_clan.mc.math.Point2i;
import de.funky_clan.mc.math.Point3d;
import de.funky_clan.mc.math.Point3i;
import de.funky_clan.mc.ui.renderer.Renderer;

import java.awt.*;

/**
 * @author synopia
 */
public class Slice {
    @Inject
    private Model     model;
    private int       slice;
    private SliceType type;

    public void setType(SliceType type) {
        this.type = type;
    }

    public void setSlice( int slice ) {
        this.slice = slice;
    }

    public Point3i sliceToWorld( Point3i slicedPos ) {
        Point3i result = null;
        switch (type) {
            case X:
                result = new Point3i( slicedPos.z(), slicedPos.y(), slicedPos.x() );
                break;
            case Y:
                result = new Point3i( slicedPos.x(), slicedPos.y(), slicedPos.z() );
                break;
            case Z:
                result = new Point3i( slicedPos.x(), slicedPos.z(), slicedPos.y() );
                break;
        }
        return result;
    }

    public Point3i worldToSlice( Point3i worldPos ) {
        return  sliceToWorld(worldPos);
    }

    public Point3d sliceToWorld( Point3d slicedPos ) {
        Point3d result = null;
        switch (type) {
            case X:
                result = new Point3d( slicedPos.z(), slicedPos.y(), slicedPos.x() );
                break;
            case Y:
                result = new Point3d( slicedPos.x(), slicedPos.y(), slicedPos.z() );
                break;
            case Z:
                result = new Point3d( slicedPos.x(), slicedPos.z(), slicedPos.y() );
                break;
        }
        return result;
    }

    public Point3d worldToSlice( Point3d worldPos ) {
        return  sliceToWorld(worldPos);
    }

    public void setPixel( Point2i slicePos, int slice, PixelType type, int value ) {
        Point3i worldPos = sliceToWorld(new Point3i(slicePos.x(), slicePos.y(), slice));
        model.setPixel(worldPos, type, value );
    }
    public int getPixel( Point2i slicePos, int slice, PixelType type ) {
        Point3i worldPos = sliceToWorld(new Point3i(slicePos.x(), slicePos.y(), slice));
        return model.getPixel(worldPos, type);
    }
    public void setPixel( Point2i slicePos, PixelType type, int value ) {
        setPixel(slicePos, slice, type, value);
    }
    public int getPixel( Point2i slicePos, PixelType type ) {
        return getPixel(slicePos, slice, type );
    }

    public SliceType getType() {
        return type;
    }
}
