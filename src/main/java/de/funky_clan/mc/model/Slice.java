package de.funky_clan.mc.model;

//~--- JDK imports ------------------------------------------------------------

import com.google.inject.Inject;
import de.funky_clan.mc.math.Position;

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

    public void setPixel( Position pos, int value ) {
        model.setPixel(pos.getBlockX(), pos.getBlockY(), pos.getBlockZ(), value );
    }
    public int getPixel( Position pos) {
        return model.getPixel(pos.getBlockX(), pos.getBlockY(), pos.getBlockZ());
    }

    public SliceType getType() {
        return type;
    }

    public int getSlice() {
        return slice;
    }
}
