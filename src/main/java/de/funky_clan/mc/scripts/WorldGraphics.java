package de.funky_clan.mc.scripts;

import com.google.inject.Inject;
import de.funky_clan.mc.model.Model;

/**
 * @author synopia
 */
public class WorldGraphics extends Graphics {
    @Inject
    private Model model;

    @Override
    public void setPixelLocal( double x, double y, double z, int value ) {
        model.setPixel( (int) ( x ), (int) ( y ), (int) ( z ), 1, value );
    }

    @Override
    public int getPixelLocal( double x, double y, double z ) {
        return model.getPixel( (int) ( x ), (int) ( y ), (int) ( z ), 1 );
    }
}
