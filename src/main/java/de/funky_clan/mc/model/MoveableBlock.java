package de.funky_clan.mc.model;

import java.awt.*;

/**
 * @author synopia
 */
public abstract class MoveableBlock {
    private double       height;    // in z
    private double       positionX;
    private double       positionY;
    private double       positionZ;
    private double       sizeX;
    private double       sizeY;

    public double getHeight() {
        return height;
    }

    public void setHeight( double height ) {
        this.height = height;
    }

    public double getSizeX() {
        return sizeX;
    }

    public void setSizeX( double sizeX ) {
        this.sizeX = sizeX;
    }

    public double getSizeY() {
        return sizeY;
    }

    public void setSizeY( double sizeY ) {
        this.sizeY = sizeY;
    }

    public void setPosition( double x, double y, double z ) {
        positionX = x;
        positionY = y;
        positionZ = z;
    }

    public double getPositionX() {
        return positionX;
    }

    public double getPositionY() {
        return positionY;
    }

    public double getPositionZ() {
        return positionZ;
    }

    public abstract Color getColor();
}
