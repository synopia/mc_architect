package de.funky_clan.mc.scripts;

import com.google.inject.Inject;
import de.funky_clan.mc.model.Model;

/**
 * @author synopia
 */
public class WorldGraphics extends Graphics {
    @Inject
    private Model model;

    private double originX;
    private double originY;
    private double originZ;

    public void setOrigin( double x, double y, double z) {
        originX = x;
        originY = y;
        originZ = z;
    }

    @Override
    public void setPixel(double x, double y, double z, int value) {
        model.setPixel((int)(x+originX), (int)(y+originY), (int)(z+originZ), 1, value );
    }

    @Override
    public int getPixel(double x, double y, double z, int value) {
        return model.getPixel((int)(x+originX), (int)(y+originY), (int)(z+originZ), 1);
    }
}
