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
    public void setPixel(double x, double y, double z, int value) {
        model.setPixel((int)x, (int)y, (int)z, value );
    }

    @Override
    public int getPixel(double x, double y, double z, int value) {
        return model.getPixel((int)x, (int)y, (int)z );
    }
}
