package de.funky_clan.mc.model;

import com.google.inject.Inject;
import de.funky_clan.mc.math.Position;

/**
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
