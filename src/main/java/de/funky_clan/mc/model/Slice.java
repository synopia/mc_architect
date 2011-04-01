package de.funky_clan.mc.model;

import com.google.inject.Inject;
import de.funky_clan.mc.math.Position;

/**
 * <p>A slice is the projection world coordinate (3d) to one specific slice (2d). A slice is defined by the used
 * plane (X-Z, Y-Z, X-Y) and the "height" of the slice (sliceNo).</p>
 *
 * @author synopia
 */
public class Slice {
    private int       maxRenderDepth;
    @Inject
    private Model     model;
    private int       slice;
    private SliceType type;

    public void setType( SliceType type ) {
        this.type = type;
    }

    public void setSlice( int slice ) {
        this.slice = slice;
    }

    public int getMaxRenderDepth() {
        return maxRenderDepth;
    }

    public void setMaxRenderDepth( int maxRenderDepth ) {
        this.maxRenderDepth = maxRenderDepth;
    }

    public void setPixel( Position pos, int type, int value ) {
        model.setPixel( pos.getBlockX(), pos.getBlockY(), pos.getBlockZ(), type, value );
    }

    public int getPixel( Position pos, int type ) {
        return model.getPixel( pos.getBlockX(), pos.getBlockY(), pos.getBlockZ(), type );
    }

    public SliceType getType() {
        return type;
    }

    public int getSlice() {
        return slice;
    }
}
