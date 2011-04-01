package de.funky_clan.mc.scripts;

import com.google.inject.Inject;
import de.funky_clan.mc.math.Position;
import de.funky_clan.mc.model.Slice;
import de.funky_clan.mc.model.SliceType;

/**
 * @author synopia
 */
public class SliceGraphics extends Graphics {
    private Position pos = new Position();
    @Inject
    private Slice    slice;

    @Override
    public void setPixelLocal( double x, double y, double z, int value ) {
        pos.setSlice( x, y, (int) z );
        slice.setPixel( pos, 1, value );
    }

    @Override
    public int getPixelLocal( double x, double y, double z ) {
        pos.setSlice( x, y, (int) z );

        return slice.getPixel( pos, 1 );
    }

    public void setSliceType( SliceType sliceType ) {
        slice.setType( sliceType );
        pos.setSlice( slice );
    }
}
