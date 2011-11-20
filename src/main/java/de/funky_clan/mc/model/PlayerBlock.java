package de.funky_clan.mc.model;

import com.google.inject.Inject;
import de.funky_clan.mc.config.Colors;

import java.awt.Color;

/**
 *
 * @author synopia
 */
public class PlayerBlock extends SelectedBlock {
    @Inject
    private Colors  colors;
    private int     direction;
    private boolean drawViewCone;
    private int     z;

    public void setDrawViewCone( boolean drawViewCone ) {
        this.drawViewCone = drawViewCone;
    }

    public boolean isDrawViewCone() {
        return drawViewCone;
    }

    public int getZ() {
        return z;
    }

    public void setZ( int z ) {
        this.z = z;
    }

    @Override
    public Type getType() {
        return SelectedBlock.Type.CENTERED;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection( int direction ) {
        this.direction = direction + 90;
    }

    @Override
    public double getHeight() {
        return 1.8;
    }

    @Override
    public double getSizeX() {
        return 0.7;
    }

    @Override
    public double getSizeY() {
        return 0.7;
    }

    @Override
    public Color getColor() {
        return colors.getPlayerBlockColor();
    }

    public void move( double dx, double dy, double dz ) {
        setPosition( getPositionX()+dx, getPositionY()+dy, getPositionZ()+dz );
    }
}
